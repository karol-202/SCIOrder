package pl.karol202.sciorder.client.android.admin.ui.adapter

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.item_order.*
import pl.karol202.sciorder.client.android.admin.R
import pl.karol202.sciorder.client.android.admin.ui.DividerItemDecorationWithoutLast
import pl.karol202.sciorder.client.android.common.ui.adapter.BasicAdapter
import pl.karol202.sciorder.client.android.common.ui.adapter.DynamicAdapter
import pl.karol202.sciorder.client.android.common.ui.setOnItemSelectedListener
import pl.karol202.sciorder.client.common.model.OrderWithProducts
import pl.karol202.sciorder.common.model.Order

class OrderAdapter(private val orderStatusUpdateListener: (Long, Order.Status) -> Unit) : DynamicAdapter<OrderWithProducts>()
{
	inner class ViewHolder(view: View) : BasicAdapter.ViewHolder<OrderWithProducts>(view)
	{
		init
		{
			spinnerOrderStatus.adapter = OrderStatusAdapter()

			recyclerOrder.layoutManager = LinearLayoutManager(ctx)
			recyclerOrder.addItemDecoration(DividerItemDecorationWithoutLast(ctx))
		}

		override fun bind(item: OrderWithProducts)
		{
			spinnerOrderStatus.setOnItemSelectedListener {
				val newStatus = it as Order.Status
				if(newStatus == item.status) return@setOnItemSelectedListener
				orderStatusUpdateListener(item.id, newStatus)
			}
			spinnerOrderStatus.setSelection(item.status.ordinal)

			textOrderLocationValue.text = item.details.location

			textOrderRecipientValue.text = item.details.recipient

			recyclerOrder.adapter = OrderEntryAdapter(item.entries)
		}
	}

	var orders: List<OrderWithProducts>
		get() = items
		set(value) { items = value }

	override fun getLayout(viewType: Int) = R.layout.item_order

	override fun createViewHolder(view: View, viewType: Int) = ViewHolder(view)

	override fun getItemId(item: OrderWithProducts) = item.id
}
