package pl.karol202.sciorder.client.js.common.view.admin

import com.ccfraser.muirwik.components.MColor
import com.ccfraser.muirwik.components.dialog.mDialogActions
import com.ccfraser.muirwik.components.dialog.mDialogContent
import com.ccfraser.muirwik.components.dialog.mDialogContentText
import com.ccfraser.muirwik.components.dialog.mDialogTitle
import com.ccfraser.muirwik.components.mButton
import pl.karol202.sciorder.client.js.common.util.dialog
import pl.karol202.sciorder.client.js.common.util.prop
import pl.karol202.sciorder.client.js.common.view.View
import pl.karol202.sciorder.client.js.common.viewmodel.OrdersJsViewModel
import pl.karol202.sciorder.client.js.common.viewmodel.ProductsJsViewModel
import pl.karol202.sciorder.common.Order
import pl.karol202.sciorder.common.Product
import react.RBuilder
import react.RProps
import react.RState
import react.setState

class AdminView(props: Props) : View<AdminView.Props, AdminView.State>(props)
{
	interface Props : RProps
	{
		var productsViewModel: ProductsJsViewModel
		var ordersViewModel: OrdersJsViewModel
	}
	
	interface State : RState
	{
		var anyOrdersPresent: Boolean
		var filteredOrders: List<Order>
		var orderFilter: Set<Order.Status>
		
		var products: List<Product>
		
		var ordersDeleteDialogOpen: Boolean
		
		var lastDeletedProduct: Product?
		var productDeleteDialogOpen: Boolean
	}
	
	private val productsViewModel by prop { productsViewModel }
	private val ordersViewModel by prop { ordersViewModel }
	
	init
	{
		state.anyOrdersPresent = false
		state.filteredOrders = emptyList()
		state.orderFilter = emptySet()
		state.products = emptyList()
		state.ordersDeleteDialogOpen = false
		state.productDeleteDialogOpen = false
		
		ordersViewModel.unfilteredOrdersObservable.bindToState { anyOrdersPresent = it?.isNotEmpty() ?: false }
		ordersViewModel.ordersObservable.bindToState { filteredOrders = it ?: emptyList() }
		ordersViewModel.orderFilterObservable.bindToState { orderFilter = it }
		
		productsViewModel.productsObservable.bindToState { products = it ?: emptyList() }
	}
	
	override fun RBuilder.render()
	{
		//ordersView()
		productsView()
		ordersDeleteDialog()
		productDeleteDialog()
	}
	
	private fun RBuilder.ordersView() = ordersView(orders = state.filteredOrders,
	                                               products = state.products,
	                                               filter = state.orderFilter,
	                                               deleteEnabled = state.anyOrdersPresent,
	                                               onDeleteAll = this@AdminView::showOrdersDeleteDialog,
	                                               onRefresh = this@AdminView::refreshOrders,
	                                               onFilterToggle = ordersViewModel::toggleOrderFilter,
	                                               onStatusUpdate = this@AdminView::updateOrderStatus)
	
	private fun RBuilder.productsView() = productsView(products = state.products,
	                                                   onChange = { updateProduct(it) },
	                                                   onDelete = { showProductDeleteDialog(it) })
	
	private fun RBuilder.ordersDeleteDialog() = dialog(open = state.ordersDeleteDialogOpen,
	                                                   onClose = { closeOrdersDeleteDialog() }) {
		mDialogTitle(text = "Na pewno chcesz usunąć wszystkie zamówienia?")
		mDialogContent {
			mDialogContentText(text = "Użytkownicy stracą możliwość śledzenia zamówień")
		}
		mDialogActions {
			mButton(caption = "Anuluj",
			        color = MColor.secondary,
			        onClick = { closeOrdersDeleteDialog() })
			mButton(caption = "Usuń",
			        color = MColor.secondary,
			        onClick = {
				        deleteAllOrders()
				        closeOrdersDeleteDialog()
			        })
		}
	}
	
	private fun RBuilder.productDeleteDialog() = state.lastDeletedProduct?.let { product ->
		dialog(open = state.productDeleteDialogOpen,
		       onClose = { closeProductDeleteDialog() }) {
			mDialogTitle(text = "Na pewno chcesz usunąć ${product.name}?")
			mDialogContent {
				mDialogContentText(text = "Produkt zniknie ze wszystkich zamówień")
			}
			mDialogActions {
				mButton(caption = "Anuluj",
				        color = MColor.secondary,
				        onClick = { closeProductDeleteDialog() })
				mButton(caption = "Usuń",
				        color = MColor.secondary,
				        onClick = {
					        deleteProduct(product)
					        closeProductDeleteDialog()
				        })
			}
		}
	}
	
	private fun refreshOrders() = ordersViewModel.refreshOrders()
	
	private fun updateOrderStatus(order: Order, status: Order.Status) = ordersViewModel.updateOrderStatus(order, status)
	
	private fun deleteAllOrders() = ordersViewModel.removeAllOrders()
	
	private fun updateProduct(product: Product) = productsViewModel.updateProduct(product)
	
	private fun deleteProduct(product: Product) = productsViewModel.removeProduct(product)
	
	private fun showOrdersDeleteDialog() = setState { ordersDeleteDialogOpen = true }
	
	private fun closeOrdersDeleteDialog() = setState { ordersDeleteDialogOpen = false }
	
	private fun showProductDeleteDialog(product: Product) = setState {
		lastDeletedProduct = product
		productDeleteDialogOpen = true
	}
	
	private fun closeProductDeleteDialog() = setState { productDeleteDialogOpen = false }
}

fun RBuilder.adminView(productsViewModel: ProductsJsViewModel,
                       ordersViewModel: OrdersJsViewModel) = child(AdminView::class) {
	attrs.productsViewModel = productsViewModel
	attrs.ordersViewModel = ordersViewModel
}
