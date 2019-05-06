package pl.karol202.sciorder.client.admin.ui.adapter

import android.view.View
import kotlinx.android.synthetic.main.item_product.*
import pl.karol202.sciorder.client.admin.R
import pl.karol202.sciorder.client.common.ui.adapter.BasicAdapter
import pl.karol202.sciorder.client.common.ui.adapter.DynamicAdapter
import pl.karol202.sciorder.common.model.Product

class ProductAdapter(private val productEditListener: (Product) -> Unit,
                     private val productRemoveListener: (Product) -> Unit) : DynamicAdapter<Product>()
{
	inner class ViewHolder(view: View) : BasicAdapter.ViewHolder<Product>(view)
	{
		override fun bind(item: Product)
		{
			textProductName.text = item.name

			buttonProductEdit.setOnClickListener { productEditListener(item) }

			buttonProductRemove.setOnClickListener { productRemoveListener(item) }
		}
	}

	override fun getLayout(viewType: Int) = R.layout.item_product

	override fun createViewHolder(view: View, viewType: Int) = ViewHolder(view)

	override fun getItemId(item: Product) = item.id
}
