package pl.karol202.sciorder.client.android.admin.ui.adapter

import android.view.View
import kotlinx.android.synthetic.main.item_order_entry_param.*
import pl.karol202.sciorder.client.android.admin.R
import pl.karol202.sciorder.client.android.common.ui.adapter.BasicAdapter
import pl.karol202.sciorder.client.android.common.ui.adapter.StaticAdapter
import pl.karol202.sciorder.common.model.Product

class OrderEntryParamAdapter(parameters: List<Pair<Product.Parameter, String?>>) :
		StaticAdapter<Pair<Product.Parameter, String?>>(parameters)
{
	class ViewHolder(view: View) : BasicAdapter.ViewHolder<Pair<Product.Parameter, String?>>(view)
	{
		private val Pair<Product.Parameter, String?>.parameter get() = first
		private val Pair<Product.Parameter, String?>.value get() = second

		override fun bind(item: Pair<Product.Parameter, String?>)
		{
			textOrderEntryParamName.text = item.parameter.name
			textOrderEntryParamValue.text = item.value?.convertToReadableForm(item.parameter.type) ?:
					ctx.getString(R.string.no_param_value)
		}

		private fun String.convertToReadableForm(type: Product.Parameter.Type) = when(type)
		{
			Product.Parameter.Type.BOOL -> if(toBoolean()) ctx.getString(R.string.text_product_param_true)
										   else ctx.getString(R.string.text_product_param_false)
			else -> this
		}
	}

	override fun getLayout(viewType: Int) = R.layout.item_order_entry_param

	override fun createViewHolder(view: View, viewType: Int) = ViewHolder(view)
}
