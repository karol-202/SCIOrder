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
import pl.karol202.sciorder.client.js.common.util.unmountOnExit
import pl.karol202.sciorder.client.js.common.view.View
import pl.karol202.sciorder.common.Product
import react.*
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
	}
	
	private val products by prop { products }
	
	private val onChange by prop { onChange }
	private val onDelete by prop { onDelete }
	
	init
	{
		state.editedProducts = emptyMap()
	}
	
	override fun RBuilder.render()
	{
		products()
	}
	
	private fun RBuilder.products() = styledDiv {
		overrideCss {
			overflow = Overflow.hidden
			children {
				firstChild {
					borderTopLeftRadius = 0.px
					borderTopRightRadius = 0.px
				}
			}
		}
		
		products.forEach { product(it) }
	}
	
	private fun RBuilder.product(product: Product) = mExpansionPanel(className = "MuiPaper-elevation2") {
		key = product.id
		unmountOnExit = true
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
		productEditView(product = getEditedOrDefaultProduct(product),
		                onUpdate = { updateProduct(it) })
	}
	
	private fun RBuilder.actionsPanel(product: Product) = mExpansionPanelActions {
		mButton(caption = "Anuluj zmiany",
		        color = MColor.secondary,
		        disabled = !isProductEdited(product),
		        onClick = { cancelChanges(product) })
		
		mButton(caption = "Zatwierdź",
		        color = MColor.secondary,
		        disabled = !isProductEdited(product) || !getEditedOrDefaultProduct(product).isValid,
		        onClick = {
			        applyChanges(product)
			        cancelChanges(product)
		        }) {
			overrideCss { marginRight = 16.px }
		}
	}
	
	private fun isProductEdited(product: Product) = getEditedProduct(product) != null
	
	private fun getEditedOrDefaultProduct(product: Product) = getEditedProduct(product) ?: product
	
	private fun getEditedProduct(product: Product) = state.editedProducts[product.id]
	
	private fun updateProduct(product: Product) = setState { editedProducts += product.id to product }
	
	private fun cancelChanges(product: Product) = setState { editedProducts -= product.id }
	
	private fun applyChanges(product: Product) = getEditedProduct(product)?.let(onChange)
}

fun RBuilder.productsView(products: List<Product>,
                          onChange: (Product) -> Unit,
                          onDelete: (Product) -> Unit) = child(ProductsView::class) {
	attrs.products = products
	attrs.onChange = onChange
	attrs.onDelete = onDelete
}
