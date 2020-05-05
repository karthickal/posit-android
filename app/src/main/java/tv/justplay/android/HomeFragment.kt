package tv.justplay.android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(), VideoSelectionListener {

    private var videoAdapter: VideoRecyclerViewAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.videoAdapter = VideoRecyclerViewAdapter(this)

        val viewModel: DiscoverViewModel = ViewModelProvider(this)[DiscoverViewModel::class.java]
        videoContent.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        videoContent.adapter = videoAdapter

        Glide.with(logoImage)
            .asBitmap()
            .load("https://s3.ap-south-1.amazonaws.com/assets.justplay.tv/jp_white_logo.png")
            .into(logoImage)

        viewModel.fetchVideoList().observe(viewLifecycleOwner, Observer {
            progressBar.visibility = View.GONE
            if (it.isNotEmpty()) {
                videoAdapter?.updateData(it)
            }
        })

        new_video_button.setOnClickListener {
            Toast.makeText(requireContext(), "Clicked", Toast.LENGTH_SHORT).show()
            showVideoUrlPopup()
        }
    }

    private fun showVideoUrlPopup() {
        findNavController().navigate(R.id.action_homeFragment_to_newVideoDialogFragment)
    }

    override fun onVideoClicked(navOptions: NavDirections) {
        findNavController().navigate(navOptions)
    }
}
