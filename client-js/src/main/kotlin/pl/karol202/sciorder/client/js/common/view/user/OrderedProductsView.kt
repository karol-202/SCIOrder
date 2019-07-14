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
import pl.karol202.sciorder.client.js.common.util.prop
import pl.karol202.sciorder.client.js.common.view.View
import react.RBuilder
import react.RProps
import react.RState
import styled.css

class OrderedProductsView : View<OrderedProductsView.Props, RState>()
{
	interface Props : RProps
	{
		var orderedProducts: List<OrderedProduct>
		var horizontalPadding: LinearDimension?
	}
	
	private val orderedProducts by prop { orderedProducts }
	private val horizontalPadding by nullableProp { horizontalPadding }
	
	override fun RBuilder.render()
	{
		mList {
			orderedProducts.forEach { product(it) }
		}
	}
	
	private fun RBuilder.product(orderedProduct: OrderedProduct) = mListItem(button = true) {
		css {
			specific { horizontalPadding?.let { padding(horizontal = it) } }
		}
		
		flexBox(direction = FlexDirection.column) {
			mTypography(text = "${orderedProduct.product.name} x${orderedProduct.quantity}")
			
			if(orderedProduct.parameters.isNotEmpty()) mList {
				orderedProduct.parameters.forEach { (name, value) -> productParam(name, value) }
			}
		}
	}
	
	private fun RBuilder.productParam(paramName: String, value: String) = mListItem {
		mTypography(text = "$paramName: $value",
		            variant = MTypographyVariant.body2)
	}
}

fun RBuilder.orderedProductsView(orderedProducts: List<OrderedProduct>,
                                 horizontalPadding: LinearDimension? = null) = child(OrderedProductsView::class) {
	attrs.orderedProducts = orderedProducts
	attrs.horizontalPadding = horizontalPadding
}
