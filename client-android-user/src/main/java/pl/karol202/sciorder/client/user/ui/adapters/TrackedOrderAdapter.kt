package pl.karol202.sciorder.client.user.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_tracked_order.*
import pl.karol202.sciorder.client.user.R
import pl.karol202.sciorder.client.user.extensions.*
import pl.karol202.sciorder.client.user.model.OrderedProduct
import pl.karol202.sciorder.common.model.Order
import pl.karol202.sciorder.common.model.Product

class TrackedOrderAdapter(private val productSupplier: (String) -> Product?,
                          private val orderRemoveListener: (Order) -> Unit) :
		RecyclerView.Adapter<TrackedOrderAdapter.ViewHolder>()
{
	inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer
	{
		private val ctx = containerView.ctx

		init
		{
			recyclerTrackedOrder.layoutManager = LinearLayoutManager(ctx)
			recyclerTrackedOrder.adapter
		}

		fun bind(order: Order, removeListener: () -> Unit)
		{
			textTrackedOrderStatusValue.text = ctx.getString(order.status.visibleName)
			textTrackedOrderStatusValue.setTextColor(ctx.getColorCompat(order.status.color))

			buttonTrackedOrderRemove.setOnClickListener { removeListener() }

			recyclerTrackedOrder.adapter = SimpleOrderAdapter(order.getOrderedProducts())
		}

		private fun Order.getOrderedProducts() =
				entries.map { OrderedProduct(randomUUIDString(),
				                             productSupplier(it.productId) ?: createPlaceholderProduct(),
				                             it.quantity,
				                             it.parameters) }

		private fun createPlaceholderProduct() =
				Product(randomUUIDString(), ctx.getString(R.string.unknown_product), false, emptyList())
	}

	var orders = emptyList<Order>()
		set(value)
		{
			val result = DiffUtil.calculateDiff(StandardDiffCallback(field, value) { _id })
			field = value
			result.dispatchUpdatesTo(this)
		}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
	{
		val view = LayoutInflater.from(parent.ctx).inflate(R.layout.item_tracked_order, parent, false)
		return ViewHolder(view)
	}

	override fun getItemCount() = orders.size

	override fun onBindViewHolder(holder: ViewHolder, position: Int)
	{
		val order = orders[position]
		holder.bind(order) { orderRemoveListener(order) }
	}
}
