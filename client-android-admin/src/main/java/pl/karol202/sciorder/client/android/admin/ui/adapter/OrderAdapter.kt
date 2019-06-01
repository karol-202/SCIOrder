package pl.karol202.sciorder.client.android.admin.ui.adapter

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.item_order.*
import pl.karol202.sciorder.client.android.admin.R
import pl.karol202.sciorder.client.android.admin.ui.DividerItemDecorationWithoutLast
import pl.karol202.sciorder.client.android.common.extensions.randomUUIDString
import pl.karol202.sciorder.client.android.common.extensions.setOnItemSelectedListener
import pl.karol202.sciorder.client.android.common.model.OrderedProduct
import pl.karol202.sciorder.client.android.common.ui.adapter.BasicAdapter
import pl.karol202.sciorder.client.android.common.ui.adapter.DynamicAdapter
import pl.karol202.sciorder.common.model.Order
import pl.karol202.sciorder.common.model.Product

class OrderAdapter(private val orderStatusUpdateListener: (Order, Order.Status) -> Unit) : DynamicAdapter<Order>()
{
	inner class ViewHolder(view: View) : BasicAdapter.ViewHolder<Order>(view)
	{
		init
		{
			spinnerOrderStatus.adapter = OrderStatusAdapter()

			recyclerOrder.layoutManager = LinearLayoutManager(ctx)
			recyclerOrder.addItemDecoration(DividerItemDecorationWithoutLast(ctx))
		}

		override fun bind(item: Order)
		{
			spinnerOrderStatus.setOnItemSelectedListener {
				val newStatus = it as Order.Status
				if(newStatus == item.status) return@setOnItemSelectedListener
				orderStatusUpdateListener(item, newStatus)
				spinnerOrderStatus.setSelection(item.status.ordinal)
			}
			spinnerOrderStatus.setSelection(item.status.ordinal)

			textOrderLocationValue.text = item.details.location

			textOrderRecipientValue.text = item.details.recipient

			recyclerOrder.adapter = OrderEntryAdapter(item.entries.map { it.toOrderedProduct() })
		}

		private fun Order.Entry.toOrderedProduct() = products.find { it.id == productId }?.let { product ->
			OrderedProduct(randomUUIDString(), product, quantity, parameters)
		}
	}

	var orders = emptyList<Order>()
		set(value)
		{
			field = value
			updateItems()
		}
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
	var orderFilter = emptySet<Order.Status>()
		set(value)
		{
			field = value
			updateItems()
		}

	override fun getLayout(viewType: Int) = R.layout.item_order

	override fun createViewHolder(view: View, viewType: Int) = ViewHolder(view)

	override fun getItemId(item: Order) = item.id

	private fun updateItems()
	{
		items = orders.filter { it.status in orderFilter }
	}
}
