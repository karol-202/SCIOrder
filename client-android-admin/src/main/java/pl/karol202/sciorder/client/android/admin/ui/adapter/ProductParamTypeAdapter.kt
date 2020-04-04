package pl.karol202.sciorder.client.android.admin.ui.adapter

import android.view.View
import kotlinx.android.synthetic.main.item_product_param_type.*
import pl.karol202.sciorder.client.android.admin.R
import pl.karol202.sciorder.client.android.common.model.visibleName
import pl.karol202.sciorder.client.android.common.ui.adapter.BasicSpinnerAdapter
import pl.karol202.sciorder.common.model.ProductParameter

class ProductParamTypeAdapter : BasicSpinnerAdapter<ProductParameter.Type>()
{
	class ViewHolder(view: View) : BasicSpinnerAdapter.ViewHolder<ProductParameter.Type>(view)
	{
		override fun bind(item: ProductParameter.Type)
		{
			textProductEditParamTypeName.text = ctx.getString(item.visibleName)
		}
	}

	override val layout = R.layout.item_product_param_type

	override fun createViewHolder(view: View) = ViewHolder(view)

	override fun getCount() = ProductParameter.Type.values().size

	override fun getItem(position: Int) = ProductParameter.Type.values()[position]
}
