package pl.karol202.sciorder.client.android.user.ui.adapter

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.item_tracked_order.*
import pl.karol202.sciorder.client.android.common.model.color
import pl.karol202.sciorder.client.android.common.model.visibleName
import pl.karol202.sciorder.client.android.common.ui.adapter.BasicAdapter
import pl.karol202.sciorder.client.android.common.ui.adapter.DynamicAdapter
import pl.karol202.sciorder.client.android.common.util.getColorCompat
import pl.karol202.sciorder.client.android.user.R
import pl.karol202.sciorder.client.common.model.OrderedProduct
import pl.karol202.sciorder.client.common.model.create
import pl.karol202.sciorder.common.Order
import pl.karol202.sciorder.common.Product

class TrackedOrderAdapter(private val orderRemoveListener: (Order) -> Unit) : DynamicAdapter<Order>()
{
	inner class ViewHolder(view: View) : BasicAdapter.ViewHolder<Order>(view)
	{
		init
		{
			recyclerTrackedOrder.layoutManager = LinearLayoutManager(ctx)
			recyclerTrackedOrder.adapter
		}

		override fun bind(item: Order)
		{
			textTrackedOrderStatusValue.text = ctx.getString(item.status.visibleName)
			textTrackedOrderStatusValue.setTextColor(ctx.getColorCompat(item.status.color))

			buttonTrackedOrderRemove.setOnClickListener { orderRemoveListener(item) }

			recyclerTrackedOrder.adapter = SimpleOrderedProductAdapter(item.entries.map { it.getOrderedProduct() })
		}

		private fun Order.Entry.getOrderedProduct() =
				OrderedProduct.create(products.find { it.id == productId } ?: createPlaceholderProduct(),
				                      quantity,
				                      parameters)

		private fun createPlaceholderProduct() = Product.create(name = ctx.getString(R.string.unknown_product))
	}

	var orders: List<Order>
		get() = items
		set(value) { items = value }
	var products = emptyList<Product>()
		set(value)
		{
			val addedProducts = value - field
			val removedProducts = field - value
			val affectedIds = (addedProducts + removedProducts).map { it.id }.distinct()
			field = value
			items.withIndex().filter { (_, order) ->
				order.entries.any { entry -> entry.productId in affectedIds }
			}.forEach { notifyItemChanged(it.index) }
		}

	override fun getLayout(viewType: Int) = R.layout.item_tracked_order

	override fun createViewHolder(view: View, viewType: Int) = ViewHolder(view)

	override fun getItemId(item: Order) = item.id
}
