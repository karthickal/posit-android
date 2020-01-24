package tv.justplay.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*

class HelloActivity : AppCompatActivity() {

    private var videoAdapter: VideoRecyclerViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.videoAdapter = VideoRecyclerViewAdapter(_context = this)

        val viewModel = ViewModelProviders.of(this)[DiscoverViewModel::class.java]
        videoContent.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        videoContent.adapter = videoAdapter

        Glide.with(logoImage)
            .asBitmap()
            .load("https://s3.ap-south-1.amazonaws.com/assets.justplay.tv/jp_white_logo.png")
            .into(logoImage)

        viewModel.fetchVideoList().observe(this, Observer {
            if (it.isNotEmpty()) {
                videoAdapter?.updateData(it)
            }
        })
    }
}
