package pl.karol202.sciorder.client.js.common.view.admin

import com.ccfraser.muirwik.components.MTypographyVariant
import com.ccfraser.muirwik.components.expansion.mExpansionPanel
import com.ccfraser.muirwik.components.expansion.mExpansionPanelDetails
import com.ccfraser.muirwik.components.expansion.mExpansionPanelSummary
import com.ccfraser.muirwik.components.mIconButton
import com.ccfraser.muirwik.components.mTypography
import kotlinx.css.*
import materialui.icons.iconDelete
import materialui.icons.iconExpandMore
import pl.karol202.sciorder.client.js.common.util.cssFlexBox
import pl.karol202.sciorder.client.js.common.util.prop
import pl.karol202.sciorder.client.js.common.view.View
import pl.karol202.sciorder.common.Product
import react.RBuilder
import react.RProps
import react.RState
import react.buildElement
import react.dom.div
import styled.css
import styled.styledDiv

class ProductsView : View<ProductsView.Props, ProductsView.State>()
{
	interface Props : RProps
	{
		var products: List<Product>
		
		var onChange: (Product) -> Unit
		var onDelete: (Product) -> Unit
	}
	
	interface State : RState
	
	private val products by prop { products }
	
	private val onChange by prop { onChange }
	private val onDelete by prop { onDelete }
	
	override fun RBuilder.render()
	{
		products()
	}
	
	private fun RBuilder.products() = div {
		products.forEach { product(it) }
	}
	
	private fun RBuilder.product(product: Product) = mExpansionPanel {
		summaryPanel(product)
		editPanel(product)
	}
	
	private fun RBuilder.summaryPanel(product: Product) = mExpansionPanelSummary(expandIcon = summaryIcon()) {
		styledDiv {
			cssFlexBox(direction = FlexDirection.row,
			           alignItems = Align.center,
			           justifyContent = JustifyContent.spaceBetween)
			css { width = 100.pct }
			
			mTypography(text = product.name,
			            variant = MTypographyVariant.subtitle1)
			mIconButton(onClick = { onDelete(product) }) { iconDelete() }
		}
	}
	
	private fun summaryIcon() = buildElement { iconExpandMore() }
	
	private fun RBuilder.editPanel(product: Product) = mExpansionPanelDetails {
	}
}

fun RBuilder.productsView(products: List<Product>,
                          onChange: (Product) -> Unit,
                          onDelete: (Product) -> Unit) = child(ProductsView::class) {
	attrs.products = products
	attrs.onChange = onChange
	attrs.onDelete = onDelete
}
