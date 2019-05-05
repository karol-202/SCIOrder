package pl.karol202.sciorder.client.admin.ui.adapter

import android.view.View
import kotlinx.android.synthetic.main.item_product.*
import pl.karol202.sciorder.client.admin.R
import pl.karol202.sciorder.client.common.ui.adapter.BasicAdapter
import pl.karol202.sciorder.client.common.ui.adapter.DynamicAdapter
import pl.karol202.sciorder.common.model.Product

class ProductAdapter : DynamicAdapter<Product>()
{
	class ViewHolder(view: View) : BasicAdapter.ViewHolder<Product>(view)
	{
		override fun bind(item: Product)
		{
			textProductName.text = item.name
		}
	}

	override fun getLayout(viewType: Int) = R.layout.item_product

	override fun createViewHolder(view: View, viewType: Int) = ViewHolder(view)

	override fun getItemId(item: Product) = item._id
}
