package tv.justplay.android.product

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.product_view_holder.view.*
import tech.posit.android.data.repository.model.ProductInfo
import tv.justplay.android.R

class ProductAdapter(
    private val products: List<ProductInfo>
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.product_view_holder,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    class ProductViewHolder(
        private val view: View
    ) : RecyclerView.ViewHolder(view) {

        fun bind(product: ProductInfo) {
            Picasso.get()
                .load(product.img_url)
                .into(view.product_image_view)

            view.marketplace_name_text_view.text = product.marketplace
            view.brand_text_view.text = product.brand

            view.product_card.setOnClickListener {
                val intent = Intent().apply {
                    action = Intent.ACTION_VIEW
                    data = Uri.parse(product.product_url)
                }

                view.context.startActivity(intent)
            }
        }
    }
}