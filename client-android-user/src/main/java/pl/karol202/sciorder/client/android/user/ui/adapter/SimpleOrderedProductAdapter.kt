package pl.karol202.sciorder.client.android.user.ui.adapter

import android.view.View
import kotlinx.android.synthetic.main.item_ordered_product_simple.*
import pl.karol202.sciorder.client.android.user.R
import pl.karol202.sciorder.client.common.model.OrderedProduct
import pl.karol202.sciorder.client.common.ui.adapter.BasicAdapter
import pl.karol202.sciorder.client.common.ui.adapter.StaticAdapter

class SimpleOrderedProductAdapter(products: List<OrderedProduct>) : StaticAdapter<OrderedProduct>(products)
{
	class ViewHolder(view: View) : BasicAdapter.ViewHolder<OrderedProduct>(view)
	{
		override fun bind(item: OrderedProduct)
		{
			textOrderedProductSimpleName.text = item.product.name
			textOrderedProductSimpleQuantity.text = ctx.getString(R.string.text_quantity_value, item.quantity)
		}
	}

	override fun getLayout(viewType: Int) = R.layout.item_ordered_product_simple

	override fun createViewHolder(view: View, viewType: Int) = ViewHolder(view)
}
