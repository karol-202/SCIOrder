package pl.karol202.sciorder.client.android.user.ui.adapter

import android.view.View
import kotlinx.android.synthetic.main.item_product.*
import pl.karol202.sciorder.client.android.user.R
import pl.karol202.sciorder.client.common.extensions.ctx
import pl.karol202.sciorder.client.common.extensions.getColorCompat
import pl.karol202.sciorder.client.common.ui.adapter.BasicAdapter
import pl.karol202.sciorder.client.common.ui.adapter.DynamicAdapter
import pl.karol202.sciorder.common.Product

class ProductAdapter : DynamicAdapter<Product>()
{
	inner class ViewHolder(view: View) : BasicAdapter.ViewHolder<Product>(view)
	{
		override fun bind(item: Product)
		{
			textProductName.text = item.name
			textProductName.setTextColor(containerView.ctx.getColorCompat(if(item.available) R.color.product_name_default
			                                                              else R.color.product_name_unavailable))

			buttonProductOrder.visibility = if(item.available) View.VISIBLE else View.GONE
			buttonProductOrder.setOnClickListener { onProductSelectListener?.invoke(item) }
		}
	}

	var products: List<Product>
		get() = items
		set(value) { items = value }
	var onProductSelectListener: ((Product) -> Unit)? = null

	override fun getLayout(viewType: Int) = R.layout.item_product

	override fun createViewHolder(view: View, viewType: Int) = ViewHolder(view)

	override fun getItemId(item: Product) = item.id
}
