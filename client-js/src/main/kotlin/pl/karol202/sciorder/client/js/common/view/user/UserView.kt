package pl.karol202.sciorder.client.js.common.view.user

import kotlinx.css.Align
import kotlinx.css.BorderStyle
import kotlinx.css.FlexDirection
import kotlinx.css.flexGrow
import kotlinx.css.height
import kotlinx.css.pct
import kotlinx.css.properties.borderLeft
import kotlinx.css.px
import pl.karol202.sciorder.client.common.model.OrderedProduct
import pl.karol202.sciorder.client.js.common.util.Muirwik
import pl.karol202.sciorder.client.js.common.util.flexBox
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
import styled.styledDiv

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
		var products: List<Product>
		var selectedProductId: String?
		
		var orderedProducts: List<OrderedProduct>
		var pendingOrder: PendingOrder?
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
		state.products = emptyList()
		state.orderedProducts = emptyList()

		productsViewModel.productsObservable.bindToState { products = it ?: emptyList() }
		orderComposeViewModel.orderObservable.bindToState { orderedProducts = it }
	}

	override fun RBuilder.render()
	{
		flexBox(flexDirection = FlexDirection.row,
		        alignItems = Align.stretch) {
			css {
				height = 100.pct
			}
			
			styledDiv {
				css {
					flexGrow = 1.0
				}
				
				productsView(state.products, state.selectedProductId) { selectProduct(it) }
				productOrderView()
				orderDetailsView()
			}
			
			styledDiv {
				css {
					flexGrow = 0.17
					borderLeft(1.px, BorderStyle.solid, Muirwik.DIVIDER_COLOR)
				}
				
				orderComposeView(state.orderedProducts) { startFullOrder() }
			}
		}
	}

	private fun RBuilder.productOrderView() = state.products.singleOrNull { it.id == state.selectedProductId }?.let { product ->
		productOrderView(product, { startSingleOrder(it) }, { addToOrder(it) })
	}

	private fun RBuilder.orderDetailsView() = state.pendingOrder?.let { pendingOrder ->
		val products = pendingOrder.getOrderedProducts(orderComposeViewModel)
		orderDetailsView(products) { pendingOrder.apply(orderComposeViewModel, it) }
	}

	private fun selectProduct(productId: String) = setState { selectedProductId = productId }

	private fun addToOrder(orderedProduct: OrderedProduct)
	{
		orderComposeViewModel.addToOrder(orderedProduct)
		setState { selectedProductId = null }
	}

	private fun startSingleOrder(orderedProduct: OrderedProduct) = setState {
		pendingOrder = PendingOrder.Single(orderedProduct)
		selectedProductId = null
	}

	private fun startFullOrder() = setState {
		pendingOrder = PendingOrder.Full()
	}
}

fun RBuilder.userView(productsViewModel: ProductsJsViewModel,
                      orderComposeViewModel: OrderComposeJsViewModel,
                      ordersTrackViewModel: OrdersTrackJsViewModel) = child(UserView::class) {
	attrs.productsViewModel = productsViewModel
	attrs.orderComposeViewModel = orderComposeViewModel
	attrs.ordersTrackViewModel = ordersTrackViewModel
}
