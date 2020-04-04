package pl.karol202.sciorder.client.android.admin.ui.adapter

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.item_order_entry.*
import pl.karol202.sciorder.client.android.admin.R
import pl.karol202.sciorder.client.android.common.ui.adapter.BasicAdapter
import pl.karol202.sciorder.client.android.common.ui.adapter.StaticAdapter
import pl.karol202.sciorder.client.common.model.OrderEntryWithProduct

class OrderEntryAdapter(entries: List<OrderEntryWithProduct>) : StaticAdapter<OrderEntryWithProduct>(entries)
{
	private enum class ViewType(@LayoutRes val layout: Int,
	                            val viewHolderCreator: (View) -> ViewHolder<OrderEntryWithProduct>)
	{
		TYPE_ENTRY(R.layout.item_order_entry, { EntryViewHolder(it) }),
		TYPE_NULL(R.layout.item_order_entry_null, { NullViewHolder(it) });

		companion object
		{
			operator fun get(ordinal: Int) = values()[ordinal]

			fun getForItem(item: OrderEntryWithProduct) = if(item.product != null) TYPE_ENTRY.ordinal else TYPE_NULL.ordinal
		}
	}

	class EntryViewHolder(view: View) : BasicAdapter.ViewHolder<OrderEntryWithProduct>(view)
	{
		init
		{
			recyclerOrderEntryParams.layoutManager = LinearLayoutManager(ctx)
		}

		override fun bind(item: OrderEntryWithProduct)
		{
			val product = item.product ?: throw IllegalStateException()
			val parametersWithValues = product.parameters.map { it to item.parameters[it.id] }
			
			textOrderEntryName.text = product.name
			textOrderEntryQuantity.text = ctx.getString(R.string.text_quantity_value, item.quantity)

			recyclerOrderEntryParams.adapter = OrderEntryParamAdapter(parametersWithValues)
		}
	}

	class NullViewHolder(view: View) : BasicAdapter.ViewHolder<OrderEntryWithProduct>(view)

	override fun getLayout(viewType: Int) = ViewType[viewType].layout

	override fun createViewHolder(view: View, viewType: Int) = ViewType[viewType].viewHolderCreator(view)

	override fun getItemViewType(position: Int) = ViewType.getForItem(getItem(position))
}
