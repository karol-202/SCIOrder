package pl.karol202.sciorder.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_ordered_product_simple.*
import pl.karol202.sciorder.R
import pl.karol202.sciorder.extensions.ctx
import pl.karol202.sciorder.model.OrderedProduct

class SimpleOrderAdapter(private val products: List<OrderedProduct>) : RecyclerView.Adapter<SimpleOrderAdapter.ViewHolder>()
{
	class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer
	{
		private val ctx = containerView.ctx

		fun bind(orderedProduct: OrderedProduct)
		{
			textOrderedProductSimpleName.text = orderedProduct.product.name
			textOrderedProductSimpleQuantity.text = ctx.getString(R.string.text_ordered_product_quantity, orderedProduct.quantity)
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
	{
		val view = LayoutInflater.from(parent.ctx).inflate(R.layout.item_ordered_product_simple, parent, false)
		return ViewHolder(view)
	}

	override fun getItemCount() = products.size

	override fun onBindViewHolder(holder: ViewHolder, position: Int)
	{
		holder.bind(products[position])
	}
}