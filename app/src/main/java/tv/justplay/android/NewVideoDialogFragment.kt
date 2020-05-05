package tv.justplay.android

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_new_video.*

/**
 * A simple [Fragment] subclass.
 */
class NewVideoDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_video, container, false)
    }

    override fun onResume() {
        super.onResume()
        val layoutParams = dialog?.window?.attributes
        layoutParams?.apply {
            width = ViewGroup.LayoutParams.MATCH_PARENT
            height = ViewGroup.LayoutParams.WRAP_CONTENT
            y = -100
        }
        dialog?.window?.apply {
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            attributes = layoutParams
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        confirm_button.setOnClickListener {
            val url = video_url_edit_text.editableText.toString().trim()
            val title = getTitle(url)
            val id = getUniqueId(url)

            val navDirections =
                NewVideoDialogFragmentDirections.actionNewVideoDialogFragmentToVideoFragment(
                    title = title, mpdUrl = url, id = id
                )

            findNavController().navigate(navDirections)
        }
    }

    private fun getUniqueId(url: String): String {
        val hashCode: Long = url.hashCode().toLong()
        return if (hashCode > 0) {
            "p_${hashCode}"
        } else {
            "n_${hashCode * -1}"
        }
    }

    private fun getTitle(url: String): String {
        val title = url.split("/").last().split(".").first()
        val code = getUniqueId(url).takeLast(3)
        return title + code
    }
}
