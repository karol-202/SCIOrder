package pl.karol202.sciorder.client.android.admin.ui.adapter

import android.view.View
import kotlinx.android.synthetic.main.item_order_entry_param.*
import pl.karol202.sciorder.client.android.admin.R
import pl.karol202.sciorder.client.android.common.ui.adapter.BasicAdapter
import pl.karol202.sciorder.client.android.common.ui.adapter.StaticAdapter
import pl.karol202.sciorder.common.model.ProductParameter

class OrderEntryParamAdapter(parameters: List<Pair<ProductParameter, String?>>) :
		StaticAdapter<Pair<ProductParameter, String?>>(parameters)
{
	class ViewHolder(view: View) : BasicAdapter.ViewHolder<Pair<ProductParameter, String?>>(view)
	{
		private val Pair<ProductParameter, String?>.parameter get() = first
		private val Pair<ProductParameter, String?>.value get() = second

		override fun bind(item: Pair<ProductParameter, String?>)
		{
			textOrderEntryParamName.text = item.parameter.name
			textOrderEntryParamValue.text = item.value?.convertToReadableForm(item.parameter.type) ?:
					ctx.getString(R.string.no_param_value)
		}

		private fun String.convertToReadableForm(type: ProductParameter.Type) = when(type)
		{
			ProductParameter.Type.BOOL -> toBoolean().toText()
			else -> this
		}
		
		private fun Boolean.toText() =
				if(this) ctx.getString(R.string.text_product_param_true)
				else ctx.getString(R.string.text_product_param_false)
	}

	override fun getLayout(viewType: Int) = R.layout.item_order_entry_param

	override fun createViewHolder(view: View, viewType: Int) = ViewHolder(view)
}
