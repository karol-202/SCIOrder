package pl.karol202.sciorder.client.android.admin.ui.adapter

import android.view.View
import kotlinx.android.synthetic.main.item_order_status.*
import pl.karol202.sciorder.client.android.admin.R
import pl.karol202.sciorder.client.android.common.model.color
import pl.karol202.sciorder.client.android.common.model.visibleName
import pl.karol202.sciorder.client.android.common.ui.adapter.BasicSpinnerAdapter
import pl.karol202.sciorder.client.android.common.util.getColorCompat
import pl.karol202.sciorder.common.Order

class OrderStatusAdapter : BasicSpinnerAdapter<Order.Status>()
{
	class ViewHolder(view: View) : BasicSpinnerAdapter.ViewHolder<Order.Status>(view)
	{
		override fun bind(item: Order.Status)
		{
			textOrderStatusItem.setText(item.visibleName)
			textOrderStatusItem.setTextColor(ctx.getColorCompat(item.color))
		}
	}

	override val layout = R.layout.item_order_status

	override fun createViewHolder(view: View) = ViewHolder(view)

	override fun getCount() = Order.Status.values().size

	override fun getItem(position: Int) = Order.Status.values()[position]
}
