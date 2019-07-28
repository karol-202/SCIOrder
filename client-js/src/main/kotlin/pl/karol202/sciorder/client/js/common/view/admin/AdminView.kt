package pl.karol202.sciorder.client.js.common.view.admin

import com.ccfraser.muirwik.components.*
import com.ccfraser.muirwik.components.dialog.mDialogActions
import com.ccfraser.muirwik.components.dialog.mDialogContent
import com.ccfraser.muirwik.components.dialog.mDialogContentText
import com.ccfraser.muirwik.components.dialog.mDialogTitle
import kotlinx.css.*
import materialui.icons.iconAdd
import materialui.icons.iconRefresh
import pl.karol202.sciorder.client.common.viewmodel.ProductsEditViewModel
import pl.karol202.sciorder.client.js.common.util.*
import pl.karol202.sciorder.client.js.common.view.View
import pl.karol202.sciorder.client.js.common.viewmodel.OrdersJsViewModel
import pl.karol202.sciorder.client.js.common.viewmodel.ProductsEditJsViewModel
import pl.karol202.sciorder.common.Order
import pl.karol202.sciorder.common.Product
import react.RBuilder
import react.RProps
import react.RState
import react.setState
import styled.css
import styled.styledDiv

class AdminView(props: Props) : View<AdminView.Props, AdminView.State>(props)
{
	interface Props : RProps
	{
		var productsEditViewModel: ProductsEditJsViewModel
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
		
		var lastMessage: Message?
		var messageShown: Boolean
	}
	
	enum class Message(val text: String,
	                   val color: Color)
	{
		PRODUCTS_UPDATE_SUCCESS("Zaktualizowano pomyślnie", Colors.Green.shade600),
		PRODUCTS_UPDATE_FAILURE("Błąd aktualizacji", Color(currentTheme.palette.error.main)),
		PRODUCTS_LOADING_FAILURE("Błąd ładowania", Color(currentTheme.palette.error.main)),
		ORDERS_LOADING_FAILURE("Błąd ładowania", Color(currentTheme.palette.error.main)),
		ORDERS_UPDATE_FAILURE("Błąd aktualizacji", Color(currentTheme.palette.error.main)),
	}
	
	private val productsEditViewModel by prop { productsEditViewModel }
	private val ordersViewModel by prop { ordersViewModel }
	
	init
	{
		state.anyOrdersPresent = false
		state.filteredOrders = emptyList()
		state.orderFilter = emptySet()
		state.products = emptyList()
		state.ordersDeleteDialogOpen = false
		state.productDeleteDialogOpen = false
		state.messageShown = false
		
		ordersViewModel.filterObservable.bindToState { orderFilter = it }
		ordersViewModel.anyOrdersPresentObservable.bindToState { anyOrdersPresent = it }
		ordersViewModel.ordersObservable.bindToState { filteredOrders = it }
		ordersViewModel.loadingErrorEventObservable.observeEvent { showMessage(Message.ORDERS_LOADING_FAILURE) }
		ordersViewModel.updateErrorEventObservable.observeEvent { showMessage(Message.ORDERS_UPDATE_FAILURE) }
		
		productsEditViewModel.productsObservable.bindToState { products = it }
		productsEditViewModel.loadingErrorEventObservable.observeEvent { showMessage(Message.PRODUCTS_LOADING_FAILURE) }
		productsEditViewModel.updateEventObservable.observeEvent {
			showMessage(when(it)
			            {
				            ProductsEditViewModel.UpdateResult.SUCCESS -> Message.PRODUCTS_UPDATE_SUCCESS
				            ProductsEditViewModel.UpdateResult.FAILURE -> Message.PRODUCTS_UPDATE_FAILURE
			            }) }
	}
	
	override fun RBuilder.render()
	{
		ordersView()
		productsPanel()
		ordersDeleteDialog()
		productDeleteDialog()
		messageSnackbar()
	}
	
	private fun RBuilder.ordersView() = ordersView(orders = state.filteredOrders,
	                                               products = state.products,
	                                               filter = state.orderFilter,
	                                               deleteEnabled = state.anyOrdersPresent,
	                                               onDeleteAll = this@AdminView::showOrdersDeleteDialog,
	                                               onRefresh = this@AdminView::refreshOrders,
	                                               onFilterToggle = ordersViewModel::toggleOrderFilter,
	                                               onStatusUpdate = this@AdminView::updateOrderStatus)
	
	private fun RBuilder.productsPanel() = styledDiv {
		cssFlexBox(direction = FlexDirection.row)
		
		styledDiv {
			cssFlexItem(basis = 600.px.basis)
			cssFlexBox(direction = FlexDirection.column)
			css { margin(horizontal = LinearDimension.auto, vertical = 24.px) }
			
			mPaper {
				overrideCss {
					borderBottomLeftRadius = 0.px
					borderBottomRightRadius = 0.px
				}
				
				styledDiv {
					cssFlexBox(direction = FlexDirection.row,
					           alignItems = Align.center)
					css { padding(vertical = 8.px) }
					
					mTypography(text = "Produkty", variant = MTypographyVariant.h6) {
						cssFlexItem(grow = 1.0)
						overrideCss { margin(horizontal = 24.px) }
					}
					mIconButton(onClick = { newProduct() }) {
						overrideCss { marginRight = 8.px }
						iconAdd()
					}
					mIconButton(onClick = { refreshProducts() }) {
						overrideCss { marginRight = 8.px }
						iconRefresh()
					}
				}
			}
			productsView()
		}
	}
	
	private fun RBuilder.productsView() = productsView(products = state.products,
	                                                   onChange = { applyProduct(it) },
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
	
	private fun RBuilder.messageSnackbar() = mSnackbar(message = state.lastMessage?.text ?: "",
	                                                   autoHideDuration = 3000,
	                                                   open = state.messageShown,
	                                                   onClose = { _, _ -> hideMessage() }) {
		state.lastMessage?.color?.let { cssSnackbarColor(it) }
	}
	
	private fun refreshOrders() = ordersViewModel.refreshOrders()
	
	private fun refreshProducts() = productsEditViewModel.refreshProducts()
	
	private fun updateOrderStatus(order: Order, status: Order.Status) = ordersViewModel.updateOrderStatus(order, status)
	
	private fun deleteAllOrders() = ordersViewModel.removeAllOrders()
	
	private fun newProduct() = productsEditViewModel.newProduct()
	
	private fun applyProduct(product: Product) = productsEditViewModel.applyProduct(product)
	
	private fun deleteProduct(product: Product) = productsEditViewModel.removeProduct(product)
	
	private fun showOrdersDeleteDialog() = setState { ordersDeleteDialogOpen = true }
	
	private fun closeOrdersDeleteDialog() = setState { ordersDeleteDialogOpen = false }
	
	private fun showProductDeleteDialog(product: Product) = setState {
		lastDeletedProduct = product
		productDeleteDialogOpen = true
	}
	
	private fun closeProductDeleteDialog() = setState { productDeleteDialogOpen = false }
	
	private fun showMessage(message: Message) = setState {
		lastMessage = message
		messageShown = true
	}
	
	private fun hideMessage() = setState { messageShown = false }
}

fun RBuilder.adminView(productsEditViewModel: ProductsEditJsViewModel,
                       ordersViewModel: OrdersJsViewModel) = child(AdminView::class) {
	attrs.productsEditViewModel = productsEditViewModel
	attrs.ordersViewModel = ordersViewModel
}
