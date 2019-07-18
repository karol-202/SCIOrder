package pl.karol202.sciorder.client.js.common.view.admin

import com.ccfraser.muirwik.components.*
import com.ccfraser.muirwik.components.expansion.mExpansionPanel
import com.ccfraser.muirwik.components.expansion.mExpansionPanelActions
import com.ccfraser.muirwik.components.expansion.mExpansionPanelDetails
import com.ccfraser.muirwik.components.expansion.mExpansionPanelSummary
import kotlinx.css.*
import materialui.icons.iconDelete
import materialui.icons.iconExpandMore
import pl.karol202.sciorder.client.js.common.util.cssFlexBox
import pl.karol202.sciorder.client.js.common.util.overrideCss
import pl.karol202.sciorder.client.js.common.util.prop
import pl.karol202.sciorder.client.js.common.view.View
import pl.karol202.sciorder.common.Product
import react.*
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
	{
		var editedProducts: Map<String, Product>
		var productsValidity: Map<String, Boolean>
	}
	
	private val products by prop { products }
	
	private val onChange by prop { onChange }
	private val onDelete by prop { onDelete }
	
	init
	{
		state.editedProducts = emptyMap()
		state.productsValidity = emptyMap()
	}
	
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
		actionsPanel(product)
	}
	
	private fun RBuilder.summaryPanel(product: Product) = mExpansionPanelSummary(summaryIcon()) {
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
		overrideCss { paddingBottom = 0.px }
		
		productEditView(product = getEditedOrDefaultProduct(product),
		                onUpdate = { product, valid -> updateProduct(product, valid) })
	}
	
	private fun RBuilder.actionsPanel(product: Product) = mExpansionPanelActions {
		mButton(caption = "Anuluj zmiany",
		        color = MColor.secondary,
		        disabled = !isProductEdited(product),
		        onClick = { cancelChanges(product) })
		
		mButton(caption = "Zatwierdź",
		        color = MColor.secondary,
		        disabled = !isProductEdited(product) || !isProductValid(product),
		        onClick = {
			        applyChanges(product)
			        cancelChanges(product)
		        }) {
			overrideCss { marginRight = 16.px }
		}
	}
	
	private fun getEditedOrDefaultProduct(product: Product) = getEditedProduct(product) ?: product
	
	private fun isProductEdited(product: Product) = getEditedProduct(product) != null
	
	private fun getEditedProduct(product: Product) = state.editedProducts[product.id]
	
	private fun isProductValid(product: Product) = state.productsValidity[product.id] ?: true
	
	private fun updateProduct(product: Product, valid: Boolean) = setState {
		editedProducts += product.id to product
		productsValidity += product.id to valid
	}
	
	private fun cancelChanges(product: Product) = setState {
		editedProducts -= product.id
		productsValidity -= product.id
	}
	
	private fun applyChanges(product: Product) = getEditedProduct(product)?.let(onChange)
}

fun RBuilder.productsView(products: List<Product>,
                          onChange: (Product) -> Unit,
                          onDelete: (Product) -> Unit) = child(ProductsView::class) {
	attrs.products = products
	attrs.onChange = onChange
	attrs.onDelete = onDelete
}
