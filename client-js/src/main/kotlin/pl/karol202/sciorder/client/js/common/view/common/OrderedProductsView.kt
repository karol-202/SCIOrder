package pl.karol202.sciorder.client.js.common.view.common

import com.ccfraser.muirwik.components.MTypographyVariant
import com.ccfraser.muirwik.components.list.mList
import com.ccfraser.muirwik.components.list.mListItem
import com.ccfraser.muirwik.components.mIconButton
import com.ccfraser.muirwik.components.mTypography
import kotlinx.css.Align
import kotlinx.css.FlexDirection
import kotlinx.css.LinearDimension
import kotlinx.css.flexGrow
import kotlinx.css.padding
import kotlinx.css.paddingRight
import kotlinx.css.px
import materialui.icons.iconClear
import materialui.icons.iconEdit
import pl.karol202.sciorder.client.common.model.OrderedProduct
import pl.karol202.sciorder.client.js.common.util.*
import pl.karol202.sciorder.client.js.common.view.View
import pl.karol202.sciorder.common.Product
import react.RBuilder
import react.RProps
import react.RState
import styled.css
import styled.styledDiv
import kotlin.collections.component1
import kotlin.collections.component2

class OrderedProductsView : View<OrderedProductsView.Props, RState>()
{
	interface Props : RProps
	{
		var orderedProducts: List<OrderedProduct>
		var details: Boolean
		var onEdit: ((OrderedProduct) -> Unit)?
		var onDelete: ((OrderedProduct) -> Unit)?
		var horizontalPadding: LinearDimension?
	}
	
	private val orderedProducts by prop { orderedProducts }
	private val details by prop { details }
	private val onEdit by nullableProp { onEdit }
	private val onDelete by nullableProp { onDelete }
	private val horizontalPadding by nullableProp { horizontalPadding }
	
	override fun RBuilder.render()
	{
		mList {
			orderedProducts.forEach { product(it) }
		}
	}
	
	private fun RBuilder.product(orderedProduct: OrderedProduct) = mListItem(button = true) {
		key = orderedProduct.id
		overrideCss {
			if(horizontalPadding != null) padding(horizontal = horizontalPadding)
			else if(onEdit != null || onDelete != null) paddingRight = 8.px
		}
		
		styledDiv {
			cssFlexBox(direction = FlexDirection.column,
			           alignItems = Align.stretch)
			cssFlexItem(grow = 1.0)
			
			styledDiv {
				cssFlexBox(direction = FlexDirection.row,
				           alignItems = Align.center)
				
				productNameText(orderedProduct)
				if(onEdit != null) editButton(orderedProduct)
				if(onDelete != null) deleteButton(orderedProduct)
			}
			
			if(details && orderedProduct.parameters.isNotEmpty()) productParams(orderedProduct)
		}
	}
	
	private fun RBuilder.productNameText(orderedProduct: OrderedProduct) =
			mTypography(text = "${orderedProduct.product.name} x${orderedProduct.quantity}") {
				css {
					flexGrow = 1.0
				}
			}
	
	private fun RBuilder.editButton(orderedProduct: OrderedProduct) =
			mIconButton(onClick = { onEdit?.invoke(orderedProduct) }) {
				iconEdit()
			}
	
	private fun RBuilder.deleteButton(orderedProduct: OrderedProduct) =
			mIconButton(onClick = { onDelete?.invoke(orderedProduct) }) {
				iconClear()
			}
	
	private fun RBuilder.productParams(orderedProduct: OrderedProduct) = mList {
		orderedProduct.recoverParams().forEach { (param, value) -> productParam(param, value) }
	}
	
	private fun RBuilder.productParam(param: Product.Parameter, value: String) = mListItem {
		mTypography(text = "${param.name}: ${param.formatValue(value)}",
		            variant = MTypographyVariant.body2)
	}
	
	private fun OrderedProduct.recoverParams() =
			parameters.mapNotNull { (name, value) -> product.getParamByName(name)?.to(value) }.toMap()
	
	private fun Product.getParamByName(name: String) = parameters.singleOrNull { it.name == name }
	
	private fun Product.Parameter.formatValue(value: String) = when(type)
	{
		Product.Parameter.Type.BOOL -> if(value.toBoolean()) "Tak" else "Nie"
		else -> value
	}
}

fun RBuilder.orderedProductsView(orderedProducts: List<OrderedProduct>,
                                 details: Boolean = false,
                                 onEdit: ((OrderedProduct) -> Unit)? = null,
                                 onDelete: ((OrderedProduct) -> Unit)? = null,
                                 horizontalPadding: LinearDimension? = null) = child(OrderedProductsView::class) {
	attrs.orderedProducts = orderedProducts
	attrs.details = details
	attrs.onEdit = onEdit
	attrs.onDelete = onDelete
	attrs.horizontalPadding = horizontalPadding
}
