package pl.karol202.sciorder.client.android.admin.ui.adapter

import android.view.View
import kotlinx.android.synthetic.main.item_store.*
import pl.karol202.sciorder.client.android.admin.R
import pl.karol202.sciorder.client.android.common.ui.adapter.BasicAdapter
import pl.karol202.sciorder.client.android.common.ui.adapter.DynamicAdapter
import pl.karol202.sciorder.client.common.model.StoreInfo

class StoreAdapter(private val storeSelectListener: (StoreInfo) -> Unit) : DynamicAdapter<StoreInfo>()
{
	inner class ViewHolder(view: View) : BasicAdapter.ViewHolder<StoreInfo>(view)
	{
		override fun bind(item: StoreInfo)
		{
			containerView.setOnClickListener { storeSelectListener(item) }
			
			textStoreName.text = item.name
			
			textStoreProducts.text = ctx.resources.getQuantityString(R.plurals.text_products, item.productsAmount, item.productsAmount)
			
			textStoreActiveOrders.text = ctx.resources.getQuantityString(R.plurals.text_active_orders, item.activeOrdersAmount, item.activeOrdersAmount)
		}
	}
	
	var stores: List<StoreInfo>
		get() = items
		set(value) { items = value }
	
	override fun getLayout(viewType: Int) = R.layout.item_store
	
	override fun createViewHolder(view: View, viewType: Int) = ViewHolder(view)
	
	override fun getItemId(item: StoreInfo) = item.id
}
