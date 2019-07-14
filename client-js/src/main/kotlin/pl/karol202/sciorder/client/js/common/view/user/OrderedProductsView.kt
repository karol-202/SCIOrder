package pl.karol202.sciorder.client.js.common.view.user

import com.ccfraser.muirwik.components.MTypographyVariant
import com.ccfraser.muirwik.components.list.mList
import com.ccfraser.muirwik.components.list.mListItem
import com.ccfraser.muirwik.components.mTypography
import kotlinx.css.FlexDirection
import kotlinx.css.LinearDimension
import kotlinx.css.padding
import pl.karol202.sciorder.client.common.model.OrderedProduct
import pl.karol202.sciorder.client.js.common.util.flexBox
import pl.karol202.sciorder.client.js.common.util.nullableProp
import pl.karol202.sciorder.client.js.common.util.overrideCss
import pl.karol202.sciorder.client.js.common.util.prop
import pl.karol202.sciorder.client.js.common.view.View
import react.RBuilder
import react.RProps
import react.RState

class OrderedProductsView : View<OrderedProductsView.Props, RState>()
{
	interface Props : RProps
	{
		var orderedProducts: List<OrderedProduct>
		var details: Boolean
		var horizontalPadding: LinearDimension?
	}
	
	private val orderedProducts by prop { orderedProducts }
	private val details by prop { details }
	private val horizontalPadding by nullableProp { horizontalPadding }
	
	override fun RBuilder.render()
	{
		mList {
			orderedProducts.forEach { product(it) }
		}
	}
	
	private fun RBuilder.product(orderedProduct: OrderedProduct) = mListItem(button = true) {
		overrideCss {
			horizontalPadding?.let { padding(horizontal = it) }
		}
		
		flexBox(direction = FlexDirection.column) {
			productNameText(orderedProduct)
			if(details && orderedProduct.parameters.isNotEmpty()) productParams(orderedProduct)
		}
	}
	
	private fun RBuilder.productNameText(orderedProduct: OrderedProduct) =
			mTypography(text = "${orderedProduct.product.name} x${orderedProduct.quantity}")
	
	private fun RBuilder.productParams(orderedProduct: OrderedProduct) = mList {
		orderedProduct.parameters.forEach { (name, value) -> productParam(name, value) }
	}
	
	private fun RBuilder.productParam(paramName: String, value: String) = mListItem {
		mTypography(text = "$paramName: $value",
		            variant = MTypographyVariant.body2)
	}
}

fun RBuilder.orderedProductsView(orderedProducts: List<OrderedProduct>,
                                 details: Boolean = false,
                                 horizontalPadding: LinearDimension? = null) = child(OrderedProductsView::class) {
	attrs.orderedProducts = orderedProducts
	attrs.details = details
	attrs.horizontalPadding = horizontalPadding
}
