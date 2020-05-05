package tv.justplay.android

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class VideoRecyclerViewAdapter(
    private val videoSelectionListener: VideoSelectionListener
) : RecyclerView.Adapter<VideoRecyclerViewAdapter.VideoViewHolder>() {

    private var items: List<VideoInfo> = emptyList()

    class VideoViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        val titleTextView: TextView = v.findViewById(R.id.titleText)
        val featuredImageView: ImageView = v.findViewById(R.id.featuredImage)
        val runningTimerText: TextView = v.findViewById(R.id.runningTimerText)
        val playIcon: ImageView = v.findViewById(R.id.playIcon)

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): VideoViewHolder {
        // Create a new view.
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.video_card, viewGroup, false)

        return VideoViewHolder(v)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(videoViewHolder: VideoViewHolder, position: Int) {

        videoViewHolder.titleTextView.text = items[position].title

        // set image and colors
        Glide.with(videoViewHolder.featuredImageView)
            .asBitmap()
            .load(items[position].thumbnailUrl)
            .into(videoViewHolder.featuredImageView)

        videoViewHolder.featuredImageView.scaleType = ImageView.ScaleType.CENTER_CROP
        videoViewHolder.playIcon.setOnClickListener {

            val navOptions = HomeFragmentDirections.actionHomeFragmentToVideoFragment(
                title = items[position].title,
                fps = items[position].fps,
                mpdUrl = items[position].mpdUrl,
                id = items[position].id
            )
            videoSelectionListener.onVideoClicked(navOptions)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateData(newItems: List<VideoInfo>) {
        this.items = newItems
        notifyDataSetChanged()
    }

    companion object {
        private const val LOG_TAG = "VideoRecyclerViewAdapter"
    }
}