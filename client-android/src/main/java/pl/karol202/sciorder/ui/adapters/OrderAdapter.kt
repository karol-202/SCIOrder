package pl.karol202.sciorder.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_ordered_product.*
import pl.karol202.sciorder.R
import pl.karol202.sciorder.extensions.ctx
import pl.karol202.sciorder.model.OrderedProduct

class OrderAdapter : RecyclerView.Adapter<OrderAdapter.ViewHolder>()
{
	inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer
	{
		private val ctx = containerView.ctx

		fun bind(ordered: OrderedProduct)
		{
			textOrderedProductName.text = ordered.product.name
			textOrderedProductQuantity.text = ctx.getString(R.string.text_ordered_product_quantity, ordered.quantity)
			buttonOrderedProductEdit.setOnClickListener { onProductEditListener?.invoke(ordered) }
			buttonOrderedProductRemove.setOnClickListener { onProductRemoveListener?.invoke(ordered) }
		}
	}

	var orderedProducts = emptyList<OrderedProduct>()
		set(value)
		{
			val result = DiffUtil.calculateDiff(StandardDiffCallback(field, value) { id })
			field = value
			result.dispatchUpdatesTo(this)
		}
	var onProductEditListener: ((OrderedProduct) -> Unit)? = null
	var onProductRemoveListener: ((OrderedProduct) -> Unit)? = null

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
	{
		val view = LayoutInflater.from(parent.ctx).inflate(R.layout.item_ordered_product, parent, false)
		return ViewHolder(view)
	}

	override fun getItemCount() = orderedProducts.size

	override fun onBindViewHolder(holder: ViewHolder, position: Int)
	{
		val product = orderedProducts[position]
		holder.bind(product)
	}
}