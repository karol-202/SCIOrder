package pl.karol202.sciorder.client.user.ui.adapter

import android.view.View
import kotlinx.android.synthetic.main.item_ordered_product.*
import pl.karol202.sciorder.client.common.model.OrderedProduct
import pl.karol202.sciorder.client.common.ui.adapter.BasicAdapter
import pl.karol202.sciorder.client.common.ui.adapter.DynamicAdapter
import pl.karol202.sciorder.client.user.R

class OrderedProductAdapter : DynamicAdapter<OrderedProduct>()
{
	inner class ViewHolder(view: View) : BasicAdapter.ViewHolder<OrderedProduct>(view)
	{
		override fun bind(item: OrderedProduct)
		{
			textOrderedProductName.text = item.product.name
			textOrderedProductQuantity.text = ctx.getString(R.string.text_quantity_value, item.quantity)
			buttonOrderedProductEdit.setOnClickListener { onProductEditListener?.invoke(item) }
			buttonOrderedProductRemove.setOnClickListener { onProductRemoveListener?.invoke(item) }
		}
	}

	var onProductEditListener: ((OrderedProduct) -> Unit)? = null
	var onProductRemoveListener: ((OrderedProduct) -> Unit)? = null

	override fun getLayout(viewType: Int) = R.layout.item_ordered_product

	override fun createViewHolder(view: View, viewType: Int) = ViewHolder(view)

	override fun getItemId(item: OrderedProduct) = item.id
}
