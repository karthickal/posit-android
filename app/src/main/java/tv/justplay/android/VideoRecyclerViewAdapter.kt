package tv.justplay.android

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlin.coroutines.coroutineContext


class VideoRecyclerViewAdapter(_context: Context): RecyclerView.Adapter<VideoRecyclerViewAdapter.VideoViewHolder>() {

    private var items: List<VideoInfo> = emptyList()
    private val LOG_TAG = "VideoRecyclerViewAdapter"
    private val context = _context

    class VideoViewHolder(val v: View) : RecyclerView.ViewHolder(v) {

        val titleTextView: TextView
        val featuredImageView: ImageView
        val runningTimerText: TextView
        val playIcon: ImageView

        init {
            titleTextView = v.findViewById(R.id.titleText)
            featuredImageView = v.findViewById(R.id.featuredImage)
            runningTimerText = v.findViewById(R.id.runningTimerText)
            playIcon = v.findViewById(R.id.playIcon)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): VideoViewHolder {
        // Create a new view.
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.video_card, viewGroup, false)

        return VideoViewHolder(v)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(videoViewHolder: VideoViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        // set the title
        videoViewHolder.titleTextView.text = items[position].title

        // set image and colors
        Glide.with(videoViewHolder.featuredImageView)
            .asBitmap()
            .load(items[position].thumbnailUrl)
            .into(videoViewHolder.featuredImageView)

        videoViewHolder.featuredImageView.setScaleType(ImageView.ScaleType.CENTER_CROP)
        videoViewHolder.playIcon.setOnClickListener {

            val i = Intent(this.context, PlayActivity::class.java)
            i.putExtra("id",items[position].id)
            i.putExtra("mpd_url",items[position].mpdUrl)
            i.putExtra("title",items[position].title)
            i.putExtra("fps",items[position].fps)

            this.context.startActivity(i)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateData(_items: List<VideoInfo>) {
        this.items = _items
        notifyDataSetChanged()
    }
}