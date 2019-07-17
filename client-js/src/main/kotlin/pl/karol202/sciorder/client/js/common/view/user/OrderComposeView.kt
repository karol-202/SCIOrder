package pl.karol202.sciorder.client.js.common.view.user

import com.ccfraser.muirwik.components.*
import com.ccfraser.muirwik.components.list.mList
import kotlinx.css.*
import materialui.icons.iconArrowLeft
import materialui.icons.iconArrowRight
import pl.karol202.sciorder.client.common.model.OrderedProduct
import pl.karol202.sciorder.client.js.common.util.*
import pl.karol202.sciorder.client.js.common.view.View
import pl.karol202.sciorder.client.js.common.view.common.orderedProductsView
import react.RBuilder
import react.RProps
import react.RState
import styled.css
import styled.styledDiv

class OrderComposeView : View<OrderComposeView.Props, RState>()
{
	interface Props : RProps
	{
		var orderedProducts: List<OrderedProduct>
		var onOrder: () -> Unit
		var onEdit: (OrderedProduct) -> Unit
		var onDelete: (OrderedProduct) -> Unit
		
		var open: Boolean
		var onOpenToggle: () -> Unit
	}

	private val orderedProducts by prop { orderedProducts }
	private val onOrder by prop { onOrder }
	private val onEdit by prop { onEdit }
	private val onDelete by prop { onDelete }
	
	private val open by prop { open }
	private val onOpenToggle by prop { onOpenToggle }

	override fun RBuilder.render()
	{
		styledDiv {
			cssFlexBox(direction = FlexDirection.column,
			           alignItems = Align.stretch)
			css {
				height = 100.pct
				overflowY = Overflow.auto
			}
			
			titlePanel()
			if(open)
			{
				divider()
				productsList()
				divider()
				orderButton()
			}
		}
	}
	
	private fun RBuilder.titlePanel() = styledDiv {
		css { position = Position.relative }
		
		if(open) titleText()
		openToggleButton()
	}
	
	private fun RBuilder.titleText() = styledDiv {
		cssFlexBox(direction = FlexDirection.column,
		           justifyContent = JustifyContent.center)
		css {
			position = Position.absolute
			left = 0.px; top = 0.px; right = 0.px; bottom = 0.px
		}
		
		mTypography(text = "Zamówienie",
		            variant = MTypographyVariant.h6,
		            align = MTypographyAlign.center)
	}
	
	private fun RBuilder.openToggleButton() = mIconButton(onClick = { onOpenToggle() }) {
		overrideCss { margin(4.px) }
		
		if(open) iconArrowRight() else iconArrowLeft()
	}
	
	private fun RBuilder.productsList() = mList {
		cssFlexItem(grow = 1.0)
		overrideCss { paddingBottom = 0.px }
		
		orderedProductsView(orderedProducts = orderedProducts,
		                    details = true,
		                    onEdit = { onEdit(it) },
		                    onDelete = { onDelete(it) })
	}
	
	private fun RBuilder.orderButton() = styledDiv {
		css {
			width = 100.pct
			padding(16.px)
		}
		mButton(caption = "Zamów",
		        fullWidth = true,
		        variant = MButtonVariant.outlined,
		        color = MColor.secondary,
		        disabled = !isOrderPossible(),
		        onClick = { orderAll() })
	}
	
	private fun orderAll()
	{
		if(isOrderPossible()) onOrder()
	}
	
	private fun isOrderPossible() = orderedProducts.isNotEmpty()
}

fun RBuilder.orderComposeView(orderedProducts: List<OrderedProduct>,
                              onOrder: () -> Unit,
                              onEdit: (OrderedProduct) -> Unit,
                              onDelete: (OrderedProduct) -> Unit,
                              open: Boolean,
                              onOpenToggle: () -> Unit) = child(OrderComposeView::class) {
	attrs.orderedProducts = orderedProducts
	attrs.onOrder = onOrder
	attrs.onEdit = onEdit
	attrs.onDelete = onDelete
	attrs.open = open
	attrs.onOpenToggle = onOpenToggle
}
