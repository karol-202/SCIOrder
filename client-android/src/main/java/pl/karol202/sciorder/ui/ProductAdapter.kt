package pl.karol202.sciorder.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_product.*
import pl.karol202.sciorder.R
import pl.karol202.sciorder.extensions.ctx
import pl.karol202.sciorder.extensions.getColorCompat
import pl.karol202.sciorder.model.Product

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ViewHolder>()
{
	inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer
	{
		fun bind(product: Product)
		{
			textProductName.text = product.name
			textProductName.setTextColor(containerView.ctx.getColorCompat(if(product.available) R.color.product_name_default
			                                                              else R.color.product_name_unavailable))

			buttonProductOrder.visibility = if(product.available) View.VISIBLE else View.GONE
			buttonProductOrder.setOnClickListener { onProductSelectListener?.invoke(product) }
		}
	}

	var products = emptyList<Product>()
		set(value)
		{
			val result = DiffUtil.calculateDiff(StandardDiffCallback(field, value) { _id })
			field = value
			result.dispatchUpdatesTo(this)
		}
	var onProductSelectListener: ((Product) -> Unit)? = null

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
	{
		val view = LayoutInflater.from(parent.ctx).inflate(R.layout.item_product, parent, false)
		return ViewHolder(view)
	}

	override fun getItemCount() = products.size

	override fun onBindViewHolder(holder: ViewHolder, position: Int)
	{
		holder.bind(products[position])
	}
}
