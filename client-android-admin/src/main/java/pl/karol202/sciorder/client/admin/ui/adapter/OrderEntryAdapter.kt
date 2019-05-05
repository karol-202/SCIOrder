package pl.karol202.sciorder.client.admin.ui.adapter

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.item_order_entry.*
import pl.karol202.sciorder.client.admin.R
import pl.karol202.sciorder.client.common.model.OrderedProduct
import pl.karol202.sciorder.client.common.ui.adapter.BasicAdapter
import pl.karol202.sciorder.client.common.ui.adapter.StaticAdapter

class OrderEntryAdapter(entries: List<OrderedProduct?>) : StaticAdapter<OrderedProduct?>(entries)
{
	private enum class ViewType(@LayoutRes val layout: Int,
	                            val viewHolderCreator: (View) -> ViewHolder<OrderedProduct?>)
	{
		TYPE_ENTRY(R.layout.item_order_entry, { EntryViewHolder(it) }),
		TYPE_NULL(R.layout.item_order_entry_null, { NullViewHolder(it) });

		companion object
		{
			operator fun get(ordinal: Int) = values()[ordinal]

			fun getForItem(item: OrderedProduct?) = if(item != null) TYPE_ENTRY.ordinal else TYPE_NULL.ordinal
		}
	}

	class EntryViewHolder(view: View) : BasicAdapter.ViewHolder<OrderedProduct?>(view)
	{
		init
		{
			recyclerOrderEntryParams.layoutManager = LinearLayoutManager(ctx)
		}

		override fun bind(item: OrderedProduct?)
		{
			if(item == null) throw IllegalArgumentException()
			textOrderEntryName.text = item.product.name
			textOrderEntryQuantity.text = ctx.getString(R.string.text_quantity_value, item.quantity)

			recyclerOrderEntryParams.adapter = OrderEntryParamAdapter(item.parametersWithValues)
		}

		private val OrderedProduct.parametersWithValues get() = product.parameters.map { it to parameters[it.name] }
	}

	class NullViewHolder(view: View) : BasicAdapter.ViewHolder<OrderedProduct?>(view)

	override fun getLayout(viewType: Int) = ViewType[viewType].layout

	override fun createViewHolder(view: View, viewType: Int) = ViewType[viewType].viewHolderCreator(view)

	override fun getItemViewType(position: Int) = ViewType.getForItem(getItem(position))
}
