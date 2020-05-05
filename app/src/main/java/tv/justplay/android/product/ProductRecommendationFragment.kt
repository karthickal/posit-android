package tv.justplay.android.product

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.product_recommendation_fragment.*
import tv.justplay.android.R
import tv.justplay.android.UIModel


class ProductRecommendationFragment : DialogFragment() {

    private lateinit var viewModel: ProductRecommendationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.product_recommendation_fragment, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        val window = dialog!!.window ?: return
        val params = window.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.MATCH_PARENT
        window.attributes = params
        window.setBackgroundDrawable(ColorDrawable(Color.WHITE))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProductRecommendationViewModel::class.java)
        close_image_view.setOnClickListener {
            this.dismiss()
        }
        viewModel.products.observe(viewLifecycleOwner, Observer {
            when (it) {
                is UIModel.FailureModel -> {
                    progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "Failed to load products", Toast.LENGTH_SHORT)
                        .show()
                    findNavController().popBackStack()
                }

                is UIModel.LoadingModel -> {
                }

                is UIModel.SuccessModel -> {
                    progressBar.visibility = View.GONE
                    if (it.data.isEmpty()) {
                        Toast.makeText(requireContext(), "No Products detected", Toast.LENGTH_SHORT)
                            .show()
                        this.dismiss()
                    }
                    product_recycler_view.adapter = ProductAdapter(it.data)
                    Log.d("products", it.data.size.toString())
                }
            }
        })
    }
}
