package tv.justplay.android

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.fragment_video.*
import tech.posit.android.data.repository.model.Product
import tech.posit.android.posit.Posit
import tech.posit.android.utils.scanVideo
import java.net.CookieHandler
import java.net.CookieManager
import java.net.CookiePolicy


class VideoFragment : Fragment() {

    private var player: SimpleExoPlayer? = null
    private lateinit var mediaDataSourceFactory: DataSource.Factory

    private var trackSelector: DefaultTrackSelector? = null
    private var lastSeenTrackGroupArray: TrackGroupArray? = null
    private val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory()
    private var currentWindow: Int = 0

    private var playbackPosition: Long = 0
    private var playWhenReady: Boolean = true

    private val LOG_TAG = "PlayActivity"
//    private val ivHideControllerButton: ImageView by lazy { findViewById<ImageView>(R.id.exo_controller) }

    private var videoId: String? = null
    private var sourceUrl: String? = null
    private var title: String? = null
    private var fps: Int = 24

    private val args: VideoFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_video, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.systemUiVisibility =
            View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        activity?.actionBar?.hide()

        this.videoId = args.id
        this.sourceUrl = args.mpdUrl
        this.fps = args.fps
        this.title = args.title

        initializePlayer()
    }

    private fun initializePlayer() {

        trackSelector = DefaultTrackSelector(requireContext(), videoTrackSelectionFactory)
        mediaDataSourceFactory = DefaultDataSourceFactory(
            requireContext(),
            Util.getUserAgent(requireContext(), "Exo2")
        )

//        var sourceUrl = "https://s3.ap-south-1.amazonaws.com/testing.deliver.videos.justplay.tv/titles/aval-anthathi/mobile/output/media.m3u8"
//        var videoId = 123
//        var fps = 24

//        var sourceUrl = "https://s3.ap-south-1.amazonaws.com/testing.deliver.videos.justplay.tv/titles/short-films/avalum-naanum/mobile/output/media.m3u8"
//        var videoId = "a234d213"
//        var fps = 24

        val hlsMediaSource = HlsMediaSource.Factory(mediaDataSourceFactory).createMediaSource(
            Uri.parse(
                sourceUrl
            )
        )

        if (CookieHandler.getDefault() != DEFAULT_COOKIE_MANAGER) {
            CookieHandler.setDefault(DEFAULT_COOKIE_MANAGER)
        }

        if ((player == null) and (trackSelector != null)) {
            player = SimpleExoPlayer.Builder(requireContext())
                .setTrackSelector(trackSelector!!)
                .build()

            player?.prepare(hlsMediaSource, false, false)
        }

        player_view?.setShutterBackgroundColor(Color.TRANSPARENT)
        player_view?.player = player
        player_view?.requestFocus()
        player?.playWhenReady = this.playWhenReady
        player_view?.useController = false

//        ivHideControllerButton.setOnClickListener { playerView.hideController() }

        Log.d(LOG_TAG, "Registering the player with Posit")
        player_view.scanVideo(
            this.videoId.toString(),
            title ?: "",
            fps,
            object : Posit.PositCallback {
                override fun onVideoShoppable(isShoppable: Boolean) {
                    Log.v("Posit", "is video shoppable: $isShoppable")
                }

                override fun onNewProduct(product: List<Product>) {
                    loader_fab.visibility = View.VISIBLE
                    loader_fab.playAnimation()

                    loader_fab.addAnimatorListener(object : Animator.AnimatorListener {
                        override fun onAnimationRepeat(animation: Animator?) {}

                        override fun onAnimationEnd(animation: Animator?) {
                            loader_fab.visibility = View.GONE
                        }

                        override fun onAnimationCancel(animation: Animator?) {}

                        override fun onAnimationStart(animation: Animator?) {}
                    })
                    Log.d("Posit new product", product.joinToString { it.productId })
                }
            }
        )


        if (playbackPosition > 0) {
            player?.seekTo(currentWindow, playbackPosition)
        }
        player_view?.useController = true
        lastSeenTrackGroupArray = null

        player!!.addListener(object : Player.EventListener {
            override fun onPlayerStateChanged(
                playWhenReady: Boolean,
                playbackState: Int
            ) {
                if (!playWhenReady || playbackState != Player.STATE_READY) {
                    if (!playWhenReady) {
                        if (findNavController().currentDestination?.id == R.id.videoFragment) {
                            findNavController().navigate(R.id.action_videoFragment_to_productRecommendationFragment)
                        }

                    } else {
                        Log.v("event", "event")
                    }
                }
            }
        })
    }


    private fun updateStartPosition() {

        playbackPosition = player?.currentPosition ?: 0
        currentWindow = player?.currentWindowIndex ?: 0
        playWhenReady = player?.playWhenReady ?: true

        Log.d(
            LOG_TAG,
            "Updating position $playbackPosition playready $playWhenReady window $currentWindow"
        )
    }

    private fun releasePlayer() {
        updateStartPosition()
        player?.release()
        player = null
        trackSelector = null
    }

    override fun onStart() {
        super.onStart()
        Log.d(LOG_TAG, "Activity Started")
        player_view?.onResume()
    }

    override fun onResume() {
        super.onResume()
        Log.d(LOG_TAG, "Activity Resumed")
    }

    override fun onPause() {
        super.onPause()
        updateStartPosition()
        Log.d(LOG_TAG, "Activity Paused")
        if (Util.SDK_INT <= 23) releasePlayer()
    }

    override fun onStop() {
        super.onStop()
//        updateStartPosition()
        Log.d(LOG_TAG, "Activity Stopped")
        if (Util.SDK_INT > 23) releasePlayer()
    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    override fun onDetach() {
        super.onDetach()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }

    companion object {
        val DEFAULT_COOKIE_MANAGER = CookieManager()
    }

    init {
        DEFAULT_COOKIE_MANAGER.setCookiePolicy(CookiePolicy.ACCEPT_ALL)
    }
}