package pl.karol202.sciorder.client.js.common.view.admin

import pl.karol202.sciorder.client.js.common.util.prop
import pl.karol202.sciorder.client.js.common.view.View
import pl.karol202.sciorder.client.js.common.viewmodel.OrdersJsViewModel
import pl.karol202.sciorder.client.js.common.viewmodel.ProductsJsViewModel
import pl.karol202.sciorder.common.Order
import pl.karol202.sciorder.common.Product
import react.RBuilder
import react.RProps
import react.RState

class AdminView(props: Props) : View<AdminView.Props, AdminView.State>(props)
{
	interface Props : RProps
	{
		var productsViewModel: ProductsJsViewModel
		var ordersViewModel: OrdersJsViewModel
	}
	
	interface State : RState
	{
		var orders: List<Order>
		
		var products: List<Product>
	}
	
	private val productsViewModel by prop { productsViewModel }
	private val ordersViewModel by prop { ordersViewModel }
	
	init
	{
		state.orders = emptyList()
		state.products = emptyList()
		
		ordersViewModel.ordersObservable.bindToState { orders = it ?: emptyList() }
		
		productsViewModel.productsObservable.bindToState { products = it ?: emptyList() }
	}
	
	override fun RBuilder.render()
	{
		ordersView()
	}
	
	private fun RBuilder.ordersView() = ordersView(orders = state.orders,
	                                               products = state.products,
	                                               onStatusUpdate = this@AdminView::updateOrderStatus,
	                                               onRefresh = this@AdminView::refreshOrders)
	
	private fun refreshOrders() = ordersViewModel.refreshOrders()
	
	private fun updateOrderStatus(order: Order, status: Order.Status) = ordersViewModel.updateOrderStatus(order, status)
}

fun RBuilder.adminView(productsViewModel: ProductsJsViewModel,
                       ordersViewModel: OrdersJsViewModel) = child(AdminView::class) {
	attrs.productsViewModel = productsViewModel
	attrs.ordersViewModel = ordersViewModel
}
