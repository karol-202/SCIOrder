package pl.karol202.sciorder.client.js.common.view.user

import com.ccfraser.muirwik.components.MButtonVariant
import com.ccfraser.muirwik.components.MColor
import com.ccfraser.muirwik.components.MTypographyAlign
import com.ccfraser.muirwik.components.MTypographyVariant
import com.ccfraser.muirwik.components.currentTheme
import com.ccfraser.muirwik.components.list.mList
import com.ccfraser.muirwik.components.mButton
import com.ccfraser.muirwik.components.mTypography
import kotlinx.css.Align
import kotlinx.css.Color
import kotlinx.css.FlexDirection
import kotlinx.css.backgroundColor
import kotlinx.css.margin
import kotlinx.css.paddingBottom
import kotlinx.css.px
import kotlinx.css.zIndex
import pl.karol202.sciorder.client.common.model.OrderedProduct
import pl.karol202.sciorder.client.js.common.util.cssFlexBox
import pl.karol202.sciorder.client.js.common.util.cssFlexItem
import pl.karol202.sciorder.client.js.common.util.cssPositionSticky
import pl.karol202.sciorder.client.js.common.util.divider
import pl.karol202.sciorder.client.js.common.util.overrideCss
import pl.karol202.sciorder.client.js.common.util.prop
import pl.karol202.sciorder.client.js.common.view.View
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
	}

	private val orderedProducts by prop { orderedProducts }
	private val onOrder by prop { onOrder }
	private val onEdit by prop { onEdit }
	private val onDelete by prop { onDelete }

	override fun RBuilder.render()
	{
		styledDiv {
			cssFlexItem(grow = 1.0)
			cssFlexBox(direction = FlexDirection.column,
			           alignItems = Align.stretch)
			
			styledDiv {
				cssFlexBox(direction = FlexDirection.column)
				cssPositionSticky(top = 64.px)
				css {
					backgroundColor = Color(currentTheme.palette.background.default)
					zIndex = 1
				}
				
				titleText()
				divider()
			}
			
			productsList()
			
			styledDiv {
				cssFlexBox(direction = FlexDirection.column,
				           alignItems = Align.stretch)
				cssPositionSticky(bottom = 0.px)
				css {
					backgroundColor = Color(currentTheme.palette.background.default)
					zIndex = 1
				}
				
				orderButton()
			}
		}
	}
	
	private fun RBuilder.titleText() = mTypography(text = "Zamówienie",
	                                               variant = MTypographyVariant.h6,
	                                               align = MTypographyAlign.center) {
		overrideCss { margin(12.px) }
	}
	
	private fun RBuilder.productsList() = mList {
		cssFlexItem(grow = 1.0)
		overrideCss { paddingBottom = 0.px }
		
		orderedProductsView(orderedProducts = orderedProducts,
		                    details = true,
		                    onEdit = { onEdit(it) },
		                    onDelete = { onDelete(it) })
	}
	
	private fun RBuilder.orderButton() = mButton(caption = "Zamów",
	                                             variant = MButtonVariant.outlined,
	                                             color = MColor.secondary,
	                                             disabled = !isOrderPossible(),
	                                             onClick = { orderAll() }) {
		overrideCss { margin(16.px) }
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
                              onDelete: (OrderedProduct) -> Unit) = child(OrderComposeView::class) {
	attrs.orderedProducts = orderedProducts
	attrs.onOrder = onOrder
	attrs.onEdit = onEdit
	attrs.onDelete = onDelete
}
