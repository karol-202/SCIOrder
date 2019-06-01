package pl.karol202.sciorder.client.android.admin.ui.adapter

import android.view.View
import kotlinx.android.synthetic.main.item_order_filter.*
import pl.karol202.sciorder.client.android.admin.R
import pl.karol202.sciorder.client.android.common.extensions.visibleName
import pl.karol202.sciorder.client.android.common.ui.adapter.BasicAdapter
import pl.karol202.sciorder.common.model.Order

class OrderFilterAdapter(filter: Set<Order.Status>) : BasicAdapter<OrderFilterAdapter.FilterEntry>()
{
	class ViewHolder(view: View) : BasicAdapter.ViewHolder<FilterEntry>(view)
	{
		override fun bind(item: FilterEntry)
		{
			checkOrderFilter.text = ctx.getString(item.status.visibleName)
			checkOrderFilter.setOnCheckedChangeListener { _, checked -> item.enabled = checked }
			checkOrderFilter.isChecked = item.enabled
		}
	}

	data class FilterEntry(val status: Order.Status,
	                       var enabled: Boolean)

	private val entries = Order.Status.values().map { FilterEntry(it, it in filter) }

	val filter get() = entries.filter { it.enabled }.map { it.status }.toSet()

	override fun getLayout(viewType: Int) = R.layout.item_order_filter

	override fun createViewHolder(view: View, viewType: Int) = ViewHolder(view)

	override fun getItem(position: Int) = entries[position]

	override fun getItemCount() = entries.size
}
