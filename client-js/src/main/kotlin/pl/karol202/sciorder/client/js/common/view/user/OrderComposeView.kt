package pl.karol202.sciorder.client.js.common.view.user

import com.ccfraser.muirwik.components.MButtonVariant
import com.ccfraser.muirwik.components.MColor
import com.ccfraser.muirwik.components.MTypographyAlign
import com.ccfraser.muirwik.components.MTypographyVariant
import com.ccfraser.muirwik.components.list.mList
import com.ccfraser.muirwik.components.mButton
import com.ccfraser.muirwik.components.mTypography
import kotlinx.css.FlexDirection
import kotlinx.css.flexGrow
import kotlinx.css.height
import kotlinx.css.margin
import kotlinx.css.pct
import kotlinx.css.px
import pl.karol202.sciorder.client.common.model.OrderedProduct
import pl.karol202.sciorder.client.js.common.util.divider
import pl.karol202.sciorder.client.js.common.util.flexBox
import pl.karol202.sciorder.client.js.common.util.overrideCss
import pl.karol202.sciorder.client.js.common.util.prop
import pl.karol202.sciorder.client.js.common.view.View
import react.RBuilder
import react.RProps
import react.RState
import styled.css

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
		flexBox(direction = FlexDirection.column) {
			css { height = 100.pct }
			
			titleText()
			divider()
			productsList()
			orderButton()
		}
	}
	
	private fun RBuilder.titleText() = mTypography(text = "Zamówienie",
	                                               variant = MTypographyVariant.h6,
	                                               align = MTypographyAlign.center) {
		overrideCss { margin(12.px) }
	}
	
	private fun RBuilder.productsList() = mList {
		css { flexGrow = 1.0 }
		
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
