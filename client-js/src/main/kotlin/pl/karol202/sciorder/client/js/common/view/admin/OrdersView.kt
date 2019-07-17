package pl.karol202.sciorder.client.js.common.view.admin

import com.ccfraser.muirwik.components.*
import com.ccfraser.muirwik.components.card.mCard
import com.ccfraser.muirwik.components.list.mList
import kotlinx.css.*
import materialui.icons.iconRefresh
import pl.karol202.sciorder.client.common.model.OrderedProduct
import pl.karol202.sciorder.client.js.common.model.color
import pl.karol202.sciorder.client.js.common.model.create
import pl.karol202.sciorder.client.js.common.model.visibleName
import pl.karol202.sciorder.client.js.common.util.cssFlexBox
import pl.karol202.sciorder.client.js.common.util.cssFlexItem
import pl.karol202.sciorder.client.js.common.util.overrideCss
import pl.karol202.sciorder.client.js.common.util.prop
import pl.karol202.sciorder.client.js.common.view.View
import pl.karol202.sciorder.client.js.common.view.common.orderedProductsView
import pl.karol202.sciorder.common.Order
import pl.karol202.sciorder.common.Product
import react.RBuilder
import react.RProps
import react.RState
import styled.css
import styled.styledDiv

class OrdersView : View<OrdersView.Props, RState>()
{
	interface Props : RProps
	{
		var orders: List<Order>
		var products: List<Product>
		
		var onStatusUpdate: (Order, Order.Status) -> Unit
		var onRefresh: () -> Unit
	}
	
	companion object
	{
		private val PLACEHOLDER_PRODUCT = Product("", "", "Usunięty produkt", false, emptyList())
	}
	
	private val orders by prop { orders }
	private val products by prop { products }
	
	private val onStatusUpdate by prop { onStatusUpdate }
	private val onRefresh by prop { onRefresh }
	
	override fun RBuilder.render()
	{
		styledDiv {
			cssFlexBox(direction = FlexDirection.column)
			
			titlePanel()
			orders()
		}
	}
	
	private fun RBuilder.titlePanel() = styledDiv {
		cssFlexBox(direction = FlexDirection.row,
		           alignItems = Align.center)
		
		titleText()
		refreshButton()
	}
	
	private fun RBuilder.titleText() = mTypography(text = "Wszystkie zamówienia",
	                                               variant = MTypographyVariant.h6) {
		cssFlexItem(grow = 1.0)
		overrideCss { margin(16.px) }
	}
	
	private fun RBuilder.refreshButton() = mIconButton(onClick = { onRefresh() }) {
		overrideCss { marginRight = 8.px }
		iconRefresh()
	}
	
	private fun RBuilder.orders() = mList {
		cssFlexBox(direction = FlexDirection.row,
		           wrap = FlexWrap.wrap)
		
		overrideCss { padding(horizontal = 4.px) }
		
		orders.forEach { order(it) }
	}
	
	private fun RBuilder.order(order: Order) = mCard {
		css {
			cssFlexItem(shrink = 0.0)
			margin(left = 12.px, right = 12.px, bottom = 24.px)
		}
		
		orderStatusPanel(order)
		products(order)
	}
	
	private fun RBuilder.orderStatusPanel(order: Order) = styledDiv {
		cssFlexBox(direction = FlexDirection.row,
		           alignItems = Align.center)
		css { margin(horizontal = 24.px, vertical = 16.px) }
		
		orderStatusText(order.status)
		orderStatusSelect(order)
	}
	
	private fun RBuilder.orderStatusText(status: Order.Status) =
			mTypography(text = "Status:",
			            variant = MTypographyVariant.subtitle2) {
		overrideCss {
			margin(right = 8.px, bottom = 3.px)
			color = status.color
		}
	}
	
	private fun RBuilder.orderStatusSelect(order: Order) = mSelect(value = order.status.name,
	                                                               onChange = { e, _ -> updateStatus(order, e.targetValue.toString()) }) {
		Order.Status.values().forEach { orderStatusItem(it) }
	}
	
	private fun RBuilder.orderStatusItem(status: Order.Status) = mMenuItem(value = status.name) {
		mTypography(text = status.visibleName,
		            variant = MTypographyVariant.subtitle2) {
			css { color = status.color }
		}
	}
	
	private fun RBuilder.products(order: Order) = orderedProductsView(orderedProducts = order.getOrderedProducts(),
	                                                                  details = true,
	                                                                  horizontalPadding = 24.px)
	
	private fun Order.getOrderedProducts() = entries.map { it.getOrderedProduct() }
	
	private fun Order.Entry.getOrderedProduct() =
			OrderedProduct.create(products.find { it.id == this.productId } ?: PLACEHOLDER_PRODUCT, quantity, parameters)
	
	private fun updateStatus(order: Order, statusName: String) =
			Order.Status.getByName(statusName)?.let { onStatusUpdate(order, it) }
}

fun RBuilder.ordersView(orders: List<Order>,
                        products: List<Product>,
                        onStatusUpdate: (Order, Order.Status) -> Unit,
                        onRefresh: () -> Unit) = child(OrdersView::class) {
	attrs.orders = orders
	attrs.products = products
	attrs.onStatusUpdate = onStatusUpdate
	attrs.onRefresh = onRefresh
}
