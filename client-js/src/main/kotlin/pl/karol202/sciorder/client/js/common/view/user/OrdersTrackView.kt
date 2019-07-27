package pl.karol202.sciorder.client.js.common.view.user

import com.ccfraser.muirwik.components.MTypographyVariant
import com.ccfraser.muirwik.components.card.mCard
import com.ccfraser.muirwik.components.list.mList
import com.ccfraser.muirwik.components.mIconButton
import com.ccfraser.muirwik.components.mTypography
import kotlinx.css.*
import materialui.icons.iconClear
import materialui.icons.iconRefresh
import pl.karol202.sciorder.client.common.model.OrderedProduct
import pl.karol202.sciorder.client.common.model.create
import pl.karol202.sciorder.client.js.common.model.color
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

class OrdersTrackView : View<OrdersTrackView.Props, RState>()
{
	interface Props : RProps
	{
		var orders: List<Order>
		var products: List<Product>
		
		var onDismiss: (Order) -> Unit
		var onRefresh: () -> Unit
	}
	
	companion object
	{
		private val PLACEHOLDER_PRODUCT = Product.create(name = "Usunięty produkt")
	}
	
	private val orders by prop { orders }
	private val products by prop { products }
	private val onDismiss by prop { onDismiss }
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
	
	private fun RBuilder.titleText() = mTypography(text = "Twoje zamówienia",
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
		           wrap = FlexWrap.wrap,
		           alignItems = Align.flexStart)
		
		overrideCss { padding(horizontal = 4.px) }
		
		orders.asReversed().forEach { order(it) }
	}
	
	private fun RBuilder.order(order: Order) = mCard {
		key = order.id
		css {
			cssFlexItem(shrink = 0.0)
			margin(left = 12.px, right = 12.px, bottom = 24.px)
		}
		
		styledDiv {
			cssFlexBox(direction = FlexDirection.row,
			           alignItems = Align.center)
			
			orderStatusText(order.status)
			dismissButton(order)
		}
		products(order)
	}
	
	private fun RBuilder.orderStatusText(status: Order.Status) =
			mTypography(text = "Status: ${status.visibleName}",
			            variant = MTypographyVariant.subtitle2) {
		overrideCss {
			margin(16.px)
			color = status.color
		}
	}
	
	private fun RBuilder.dismissButton(order: Order) =
			mIconButton(onClick = { onDismiss(order) }) { iconClear() }
	
	private fun RBuilder.products(order: Order) = orderedProductsView(orderedProducts = order.getOrderedProducts())
	
	private fun Order.getOrderedProducts() = entries.map { it.getOrderedProduct() }
	
	private fun Order.Entry.getOrderedProduct() =
			OrderedProduct.create(products.find { it.id == this.productId } ?: PLACEHOLDER_PRODUCT, quantity, parameters)
}

fun RBuilder.ordersTrackView(orders: List<Order>,
                             products: List<Product>,
                             onDismiss: (Order) -> Unit,
                             onRefresh: () -> Unit) = child(OrdersTrackView::class) {
	attrs.orders = orders
	attrs.products = products
	attrs.onDismiss = onDismiss
	attrs.onRefresh = onRefresh
}
