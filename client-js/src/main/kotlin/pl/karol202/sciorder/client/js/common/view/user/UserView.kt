package pl.karol202.sciorder.client.js.common.view.user

import com.ccfraser.muirwik.components.*
import com.ccfraser.muirwik.components.dialog.mDialogTitle
import kotlinx.css.*
import kotlinx.css.properties.borderLeft
import materialui.icons.iconClear
import materialui.icons.iconRefresh
import pl.karol202.sciorder.client.common.model.OrderedProduct
import pl.karol202.sciorder.client.common.viewmodel.OrderComposeViewModel
import pl.karol202.sciorder.client.js.common.util.*
import pl.karol202.sciorder.client.js.common.view.View
import pl.karol202.sciorder.client.js.common.viewmodel.OrderComposeJsViewModel
import pl.karol202.sciorder.client.js.common.viewmodel.OrdersTrackJsViewModel
import pl.karol202.sciorder.client.js.common.viewmodel.ProductsJsViewModel
import pl.karol202.sciorder.common.Order
import pl.karol202.sciorder.common.Product
import react.*
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
		var trackedOrders: List<Order>
		
		var products: List<Product>
		var selectedProductId: String?
		
		var orderedProducts: List<OrderedProduct>
		var orderComposeViewOpen: Boolean
		
		var lastEditedProduct: OrderedProduct?
		var productEditDialogOpen: Boolean
		
		var lastPendingOrder: PendingOrder?
		var orderDialogOpen: Boolean
		
		var lastMessage: Message?
		var messageShown: Boolean
	}
	
	enum class Message(val text: String,
	                   val color: Color)
	{
		ORDER_SUCCESS("Zamówiono pomyślnie", Colors.Green.shade600),
		ORDER_FAILURE("Błąd zamówienia", Color(currentTheme.palette.error.main)),
		TRACKED_ORDERS_LOADING_FAILURE("Błąd ładowania", Color(currentTheme.palette.error.main)),
		PRODUCTS_LOADING_FAILURE("Błąd ładowania", Color(currentTheme.palette.error.main))
	}

	interface PendingOrder
	{
		class Single(private val orderedProduct: OrderedProduct) : PendingOrder
		{
			override val orderedProducts = listOf(orderedProduct)

			override fun apply(viewModel: OrderComposeJsViewModel, details: Order.Details) =
					viewModel.orderSingleProduct(orderedProduct, details)
		}

		class Full(override val orderedProducts: List<OrderedProduct>) : PendingOrder
		{
			override fun apply(viewModel: OrderComposeJsViewModel, details: Order.Details) = viewModel.orderAll(details)
		}

		val orderedProducts: List<OrderedProduct>

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
		state.orderComposeViewOpen = true
		state.productEditDialogOpen = false
		state.orderDialogOpen = false
		state.messageShown = false
		
		ordersTrackViewModel.ordersObservable.bindToState { trackedOrders = it }
		ordersTrackViewModel.errorEventObservable.observeEvent { showMessage(Message.TRACKED_ORDERS_LOADING_FAILURE) }
		
		productsViewModel.productsObservable.bindToState { products = it }
		productsViewModel.loadingErrorEventObservable.observeEvent { showMessage(Message.PRODUCTS_LOADING_FAILURE) }
		
		orderComposeViewModel.orderObservable.bindToState { orderedProducts = it }
		orderComposeViewModel.errorEventObservable.observeEvent { showMessage(when(it)
		{
			OrderComposeViewModel.OrderResult.SUCCESS -> Message.ORDER_SUCCESS
			OrderComposeViewModel.OrderResult.FAILURE -> Message.ORDER_FAILURE
		}) }
	}

	override fun RBuilder.render()
	{
		styledDiv {
			cssFlexBox(direction = FlexDirection.column,
			           alignItems = Align.stretch)
			css { marginRight = if(state.orderComposeViewOpen) 350.px else 57.px }
			
			if(state.trackedOrders.isNotEmpty())
			{
				ordersTrackView()
				divider()
			}
			productsPanel()
			productOrderPanel()
		}
		
		styledDiv {
			cssPositionFixed(right = 0.px, top = 64.px, bottom = 0.px)
			css {
				width = if(state.orderComposeViewOpen) 350.px else 57.px
				borderLeft(1.px, BorderStyle.solid, Muirwik.DIVIDER_COLOR)
			}
			
			orderComposeView()
		}
		productEditDialog()
		orderDetailsDialog()
		messageSnackbar()
	}
	
	private fun RBuilder.ordersTrackView() = ordersTrackView(orders = state.trackedOrders,
	                                                         products = state.products,
	                                                         onDismiss = { dismissTrackedOrder(it) },
	                                                         onRefresh = { refreshTrackedOrders() })
	
	private fun RBuilder.productsPanel() = panel(title = "Zamów",
	                                             actionIcon = { iconRefresh() },
	                                             action = { refreshProducts() }) { productsView() }
	
	private fun RBuilder.productOrderPanel() = state.products.singleOrNull { it.id == state.selectedProductId }?.let { product ->
		panel(title = "Zamów: ${product.name}",
		      actionIcon = { iconClear() },
		      action = { resetProductSelection() }) { productOrderView(product) }
	}
	
	private fun RBuilder.panel(title: String,
	                           actionIcon: RBuilder.() -> ReactElement,
	                           action: () -> Unit,
	                           handler: RBuilder.() -> Unit) = styledDiv {
		cssFlexBox(direction = FlexDirection.row)
		
		mPaper {
			cssFlexItem(basis = 500.px.basis)
			cssFlexBox(direction = FlexDirection.column)
			css {
				margin(horizontal = LinearDimension.auto, vertical = 24.px)
				paddingTop = 8.px
			}
			
			styledDiv {
				cssFlexBox(direction = FlexDirection.row,
				           alignItems = Align.center)
				
				mTypography(text = title, variant = MTypographyVariant.h6) {
					cssFlexItem(grow = 1.0)
					overrideCss { margin(horizontal = 24.px) }
				}
				mIconButton(onClick = { action() }) {
					overrideCss { marginRight = 8.px }
					actionIcon()
				}
				
			}
			handler()
		}
	}
	
	private fun RBuilder.productsView() = productsView(state.products, state.selectedProductId) { selectProduct(it) }

	private fun RBuilder.productOrderView(product: Product) = productOrderView(product = product,
	                                                                           onOrder = {
		                                                                           startSingleOrder(it)
		                                                                           resetProductSelection()
	                                                                           },
	                                                                           onAddToOrder = {
		                                                                           addToOrder(it)
		                                                                           resetProductSelection()
	                                                                           })
	
	private fun RBuilder.orderComposeView() = orderComposeView(orderedProducts = state.orderedProducts,
	                                                           onOrder = { startFullOrder() },
	                                                           onEdit = { editOrderedProduct(it) },
	                                                           onDelete = { removeFromOrder(it) },
	                                                           open = state.orderComposeViewOpen,
	                                                           onOpenToggle = { toggleOrderComposeViewOpen() })
	
	private fun RBuilder.productEditDialog() = dialog(open = state.productEditDialogOpen,
	                                                  onClose = { closeProductEditDialog() }) {
		mDialogTitle(text = "Edytuj produkt")
		productEditView()
	}
	
	private fun RBuilder.productEditView() = state.lastEditedProduct?.let { oldProduct ->
		val (id, product, quantity, params) = oldProduct
		
		fun fakeAttrs(param: Product.Parameter) = param.attributes.copy(defaultValue = params[param.name])
		val fakeParams = product.parameters.map { it.copy(attributes = fakeAttrs(it)) }
		val fakeProduct = product.copy(parameters = fakeParams)
		
		productOrderView(product = fakeProduct,
		                 initialQuantity = quantity,
		                 onEdit = {
			                 val newProduct = it.copy(id = id)
			                 replaceInOrder(oldProduct, newProduct)
			                 closeProductEditDialog()
		                 })
	}
	
	private fun RBuilder.orderDetailsDialog() = dialog(open = state.orderDialogOpen,
	                                                   onClose = { closeOrderDetailsDialog() }) {
		mDialogTitle(text = "Podsumowanie zamówienia")
		orderDetailsView()
	}
	
	private fun RBuilder.orderDetailsView() = state.lastPendingOrder?.let { pendingOrder ->
		orderDetailsView(orderedProducts = pendingOrder.orderedProducts,
		                 onDetailsSet = {
			                 finishOrder(pendingOrder, it)
			                 closeOrderDetailsDialog()
		                 },
		                 onCancel = { closeOrderDetailsDialog() })
	}
	
	private fun RBuilder.messageSnackbar() = mSnackbar(message = state.lastMessage?.text ?: "",
	                                                   autoHideDuration = 3000,
	                                                   open = state.messageShown,
	                                                   onClose = { _, _ -> hideMessage() }) {
		state.lastMessage?.color?.let { cssSnackbarColor(it) }
	}
	
	private fun selectProduct(productId: String) = setState { selectedProductId = productId }
	
	private fun resetProductSelection() = setState { selectedProductId = null }
	
	private fun refreshProducts() = productsViewModel.refreshProducts()
	
	private fun refreshTrackedOrders() = ordersTrackViewModel.refreshOrders()
	
	private fun dismissTrackedOrder(order: Order) = ordersTrackViewModel.removeOrder(order)
	
	private fun addToOrder(orderedProduct: OrderedProduct) = orderComposeViewModel.addToOrder(orderedProduct)
	
	private fun replaceInOrder(old: OrderedProduct, new: OrderedProduct) = orderComposeViewModel.replaceInOrder(old, new)
	
	private fun removeFromOrder(orderedProduct: OrderedProduct) = orderComposeViewModel.removeFromOrder(orderedProduct)
	
	private fun editOrderedProduct(orderedProduct: OrderedProduct) = setState {
		lastEditedProduct = orderedProduct
		productEditDialogOpen = true
	}
	
	private fun startSingleOrder(orderedProduct: OrderedProduct) = startOrder(PendingOrder.Single(orderedProduct))
	
	private fun startFullOrder() = startOrder(PendingOrder.Full(state.orderedProducts))
	
	private fun startOrder(pendingOrder: PendingOrder) = setState {
		lastPendingOrder = pendingOrder
		orderDialogOpen = true
	}
	
	private fun finishOrder(order: PendingOrder, details: Order.Details) = order.apply(orderComposeViewModel, details)
	
	private fun toggleOrderComposeViewOpen() = setState { orderComposeViewOpen = !orderComposeViewOpen }
	
	private fun closeOrderDetailsDialog() = setState { orderDialogOpen = false }
	
	private fun closeProductEditDialog() = setState { productEditDialogOpen = false }
	
	private fun showMessage(message: Message) = setState {
		lastMessage = message
		messageShown = true
	}
	
	private fun hideMessage() = setState { messageShown = false }
}

fun RBuilder.userView(productsViewModel: ProductsJsViewModel,
                      orderComposeViewModel: OrderComposeJsViewModel,
                      ordersTrackViewModel: OrdersTrackJsViewModel) = child(UserView::class) {
	attrs.productsViewModel = productsViewModel
	attrs.orderComposeViewModel = orderComposeViewModel
	attrs.ordersTrackViewModel = ordersTrackViewModel
}
