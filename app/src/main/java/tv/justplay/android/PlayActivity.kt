package tv.justplay.android

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Log
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_player.*
import tech.posit.android.data.repository.model.Product
import tech.posit.android.posit.Posit
import tech.posit.android.utils.scanVideo

class PlayActivity : AppCompatActivity() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar?.hide()

        this.videoId = intent.getStringExtra("id")
        this.sourceUrl = intent.getStringExtra("mpd_url")
        this.fps = intent.getIntExtra("fps", 24)
        this.title = intent.getStringExtra("title")
    }

    private fun initializePlayer() {

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)
        mediaDataSourceFactory = DefaultDataSourceFactory(this, Util.getUserAgent(this, "justplayTV"))

//        var sourceUrl = "https://s3.ap-south-1.amazonaws.com/testing.deliver.videos.justplay.tv/titles/aval-anthathi/mobile/output/media.m3u8"
//        var videoId = 123
//        var fps = 24

//        var sourceUrl = "https://s3.ap-south-1.amazonaws.com/testing.deliver.videos.justplay.tv/titles/short-films/avalum-naanum/mobile/output/media.m3u8"
//        var videoId = "a234d213"
//        var fps = 24

        val hlsMediaSource = HlsMediaSource.Factory(mediaDataSourceFactory).createMediaSource(Uri.parse(sourceUrl))

        if ((player == null) and (trackSelector != null)) {
            player = ExoPlayerFactory.newSimpleInstance(this, trackSelector!!)
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
            fps,
            object : Posit.PositCallback {
                override fun onVideoShoppable(isShoppable: Boolean) {

                }

                override fun onNewProduct(product: List<Product>) {
                    Toast.makeText(this@PlayActivity, product[0].name, Toast.LENGTH_SHORT).show()
                }
            }
        )
        if (playbackPosition > 0) {
            player?.seekTo(currentWindow, playbackPosition)
        }
        player_view?.useController = true
        lastSeenTrackGroupArray = null
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

    public override fun onStart() {
        super.onStart()

        Log.d(LOG_TAG, "Activity Started")
        initializePlayer()

        player_view?.onResume()
    }

    public override fun onResume() {
        super.onResume()

        Log.d(LOG_TAG, "Activity Resumed")


//        initializePlayer()
//        if (Util.SDK_INT <= 23) initializePlayer()
    }

    public override fun onPause() {
        super.onPause()
        updateStartPosition()

        Log.d(LOG_TAG, "Activity Paused")

        if (Util.SDK_INT <= 23) releasePlayer()
    }

    public override fun onStop() {
        super.onStop()
//        updateStartPosition()
        Log.d(LOG_TAG, "Activity Stopped")

        if (Util.SDK_INT > 23) releasePlayer()
    }

}