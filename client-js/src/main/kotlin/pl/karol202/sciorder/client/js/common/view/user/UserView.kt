package pl.karol202.sciorder.client.js.common.view.user

import com.ccfraser.muirwik.components.dialog.mDialogTitle
import kotlinx.css.Align
import kotlinx.css.BorderStyle
import kotlinx.css.FlexDirection
import kotlinx.css.basis
import kotlinx.css.height
import kotlinx.css.pct
import kotlinx.css.properties.borderLeft
import kotlinx.css.px
import pl.karol202.sciorder.client.common.model.OrderedProduct
import pl.karol202.sciorder.client.js.common.util.Muirwik
import pl.karol202.sciorder.client.js.common.util.dialog
import pl.karol202.sciorder.client.js.common.util.flexBox
import pl.karol202.sciorder.client.js.common.util.flexItem
import pl.karol202.sciorder.client.js.common.util.prop
import pl.karol202.sciorder.client.js.common.view.View
import pl.karol202.sciorder.client.js.common.viewmodel.OrderComposeJsViewModel
import pl.karol202.sciorder.client.js.common.viewmodel.OrdersTrackJsViewModel
import pl.karol202.sciorder.client.js.common.viewmodel.ProductsJsViewModel
import pl.karol202.sciorder.common.Order
import pl.karol202.sciorder.common.Product
import react.RBuilder
import react.RProps
import react.RState
import react.setState
import styled.css

class UserView(props: Props) : View<UserView.Props, UserView.State>(props)
{
	interface Props : RProps
	{
		var productsViewModel: ProductsJsViewModel
		var orderComposeViewModel: OrderComposeJsViewModel
		var ordersTrackViewModel: OrdersTrackJsViewModel
	}

	interface State : RState
	{
		var trackedOrders: List<Order>
		
		var products: List<Product>
		var selectedProductId: String?
		
		var orderedProducts: List<OrderedProduct>
		var lastPendingOrder: PendingOrder?
		var orderDialogOpen: Boolean
	}

	interface PendingOrder
	{
		class Single(private val orderedProduct: OrderedProduct) : PendingOrder
		{
			override fun getOrderedProducts(viewModel: OrderComposeJsViewModel) = listOf(orderedProduct)

			override fun apply(viewModel: OrderComposeJsViewModel, details: Order.Details) =
					viewModel.orderSingleProduct(orderedProduct, details)
		}

		class Full : PendingOrder
		{
			override fun getOrderedProducts(viewModel: OrderComposeJsViewModel) =
					viewModel.getProductsInOrderOrNull() ?: emptyList()

			override fun apply(viewModel: OrderComposeJsViewModel, details: Order.Details) = viewModel.orderAll(details)
		}

		fun getOrderedProducts(viewModel: OrderComposeJsViewModel): List<OrderedProduct>

		fun apply(viewModel: OrderComposeJsViewModel, details: Order.Details)
	}

	private val productsViewModel by prop { productsViewModel }
	private val orderComposeViewModel by prop { orderComposeViewModel }
	private val ordersTrackViewModel by prop { ordersTrackViewModel }

	init
	{
		state.trackedOrders = emptyList()
		state.products = emptyList()
		state.orderedProducts = emptyList()
		state.orderDialogOpen = false

		ordersTrackViewModel.ordersObservable.bindToState { trackedOrders = it ?: emptyList() }
		productsViewModel.productsObservable.bindToState { products = it ?: emptyList() }
		orderComposeViewModel.orderObservable.bindToState { orderedProducts = it }
	}

	override fun RBuilder.render()
	{
		flexBox(direction = FlexDirection.row,
		        alignItems = Align.stretch) {
			css { height = 100.pct }
			
			flexItem(flexGrow = 1.0) {
				if(state.trackedOrders.isNotEmpty()) ordersTrackView()
				productsView()
				productOrderView()
			}
			
			flexItem(flexBasis = 350.px.basis) {
				css { borderLeft(1.px, BorderStyle.solid, Muirwik.DIVIDER_COLOR) }
				orderComposeView()
			}
		}
		orderDetailsDialog()
	}
	
	private fun RBuilder.ordersTrackView() = ordersTrackView(state.trackedOrders, state.products) { ordersTrackViewModel.removeOrder(it) }
	
	private fun RBuilder.productsView() = productsView(state.products, state.selectedProductId) { selectProduct(it) }

	private fun RBuilder.productOrderView() = state.products.singleOrNull { it.id == state.selectedProductId }?.let { product ->
		productOrderView(product = product, onOrder = {
			startSingleOrder(it)
			resetProductSelection()
		}, onAddToOrder = {
			addToOrder(it)
			resetProductSelection()
		})
	}
	
	private fun RBuilder.orderDetailsDialog() = dialog(open = state.orderDialogOpen,
	                                                   onClose = { closeDialog() }) {
		mDialogTitle(text = "Podsumowanie zamówienia")
		orderDetailsView()
	}
	
	private fun RBuilder.orderDetailsView() = state.lastPendingOrder?.let { pendingOrder ->
		val products = pendingOrder.getOrderedProducts(orderComposeViewModel)
		orderDetailsView(products, { finishOrder(pendingOrder, it) }, { closeDialog() })
	}
	
	private fun RBuilder.orderComposeView() = orderComposeView(state.orderedProducts) { startFullOrder() }
	
	private fun selectProduct(productId: String) = setState { selectedProductId = productId }
	
	private fun resetProductSelection() = setState { selectedProductId = null }
	
	private fun addToOrder(orderedProduct: OrderedProduct) = orderComposeViewModel.addToOrder(orderedProduct)
	
	private fun startSingleOrder(orderedProduct: OrderedProduct) = startOrder(PendingOrder.Single(orderedProduct))
	
	private fun startFullOrder() = startOrder(PendingOrder.Full())
	
	private fun startOrder(pendingOrder: PendingOrder) = setState {
		lastPendingOrder = pendingOrder
		orderDialogOpen = true
	}
	
	private fun finishOrder(pendingOrder: PendingOrder, details: Order.Details)
	{
		pendingOrder.apply(orderComposeViewModel, details)
		closeDialog()
	}
	
	private fun closeDialog() = setState { orderDialogOpen = false }
}

fun RBuilder.userView(productsViewModel: ProductsJsViewModel,
                      orderComposeViewModel: OrderComposeJsViewModel,
                      ordersTrackViewModel: OrdersTrackJsViewModel) = child(UserView::class) {
	attrs.productsViewModel = productsViewModel
	attrs.orderComposeViewModel = orderComposeViewModel
	attrs.ordersTrackViewModel = ordersTrackViewModel
}
