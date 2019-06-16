package pl.karol202.sciorder.client.android.admin.ui.adapter

import android.view.View
import kotlinx.android.synthetic.main.item_product_param_type.*
import pl.karol202.sciorder.client.android.admin.R
import pl.karol202.sciorder.client.common.extensions.visibleName
import pl.karol202.sciorder.client.common.ui.adapter.BasicSpinnerAdapter
import pl.karol202.sciorder.common.Product

class ProductParamTypeAdapter : BasicSpinnerAdapter<Product.Parameter.Type>()
{
	class ViewHolder(view: View) : BasicSpinnerAdapter.ViewHolder<Product.Parameter.Type>(view)
	{
		override fun bind(item: Product.Parameter.Type)
		{
			textProductEditParamTypeName.text = ctx.getString(item.visibleName)
		}
	}

	override val layout = R.layout.item_product_param_type

	override fun createViewHolder(view: View) = ViewHolder(view)

	override fun getCount() = Product.Parameter.Type.values().size

	override fun getItem(position: Int) = Product.Parameter.Type.values()[position]
}
