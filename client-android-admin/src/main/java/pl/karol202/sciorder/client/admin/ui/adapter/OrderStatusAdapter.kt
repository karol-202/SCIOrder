package pl.karol202.sciorder.client.admin.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_order_status.*
import pl.karol202.sciorder.client.admin.R
import pl.karol202.sciorder.client.common.extensions.color
import pl.karol202.sciorder.client.common.extensions.ctx
import pl.karol202.sciorder.client.common.extensions.getColorCompat
import pl.karol202.sciorder.client.common.extensions.visibleName
import pl.karol202.sciorder.common.model.Order

class OrderStatusAdapter : BaseAdapter()
{
	class ViewHolder(override val containerView: View) : LayoutContainer
	{
		private val ctx = containerView.ctx

		fun bind(status: Order.Status)
		{
			textOrderStatusItem.setText(status.visibleName)
			textOrderStatusItem.setTextColor(ctx.getColorCompat(status.color))
		}
	}

	override fun getCount() = Order.Status.values().size

	override fun getItem(position: Int) = Order.Status.values()[position]

	override fun getItemId(position: Int) = position.toLong()

	override fun getView(position: Int, convertView: View?, parent: ViewGroup): View
	{
		val viewHolder = convertView?.tag as? ViewHolder ?: createViewHolder(parent)
		viewHolder.bind(getItem(position))
		return viewHolder.containerView
	}

	private fun createViewHolder(parent: ViewGroup): ViewHolder
	{
		val view = LayoutInflater.from(parent.ctx).inflate(R.layout.item_order_status, parent, false)
		return ViewHolder(view).also { view.tag = it }
	}
}
