package pl.karol202.sciorder.client.js.common.view.user

import com.ccfraser.muirwik.components.MTypographyColor
import com.ccfraser.muirwik.components.MTypographyVariant
import com.ccfraser.muirwik.components.list.mList
import com.ccfraser.muirwik.components.list.mListItem
import com.ccfraser.muirwik.components.list.mListItemText
import com.ccfraser.muirwik.components.mTypography
import pl.karol202.sciorder.client.js.common.util.nullableProp
import pl.karol202.sciorder.client.js.common.util.prop
import pl.karol202.sciorder.client.js.common.view.View
import pl.karol202.sciorder.common.Product
import react.RBuilder
import react.RProps
import react.RState
import react.buildElement
import react.setState

class UserProductsView(props: Props) : View<UserProductsView.Props, RState>(props)
{
	interface Props : RProps
	{
		var products: List<Product>
		var selectedProductId: String?
		var onProductSelect: (String) -> Unit
	}

	private val products by prop { products }
	private val selectedProductId by nullableProp { selectedProductId }
	private val onProductSelect by prop { onProductSelect }

	override fun RBuilder.render()
	{
		productsList()
	}

	private fun RBuilder.productsList() = mList {
		products.forEach { product(it) }
	}

	private fun RBuilder.product(product: Product) = mListItem(button = product.available,
	                                                           selected = product.id == selectedProductId,
	                                                           onClick = { selectProductIfAvailable(product) }) {
		mListItemText(primary = createProductName(product), disableTypography = true)
	}

	private fun selectProductIfAvailable(product: Product)
	{
		if(product.available) setState { onProductSelect(product.id) }
	}

	private fun createProductName(product: Product) = buildElement {
		mTypography(text = product.name,
		            variant = MTypographyVariant.subtitle1,
		            color = if(product.available) MTypographyColor.textPrimary else MTypographyColor.textSecondary)
	}
}

fun RBuilder.productsView(products: List<Product>,
                          selectedProductId: String?,
                          onProductSelect: (String) -> Unit) = child(UserProductsView::class) {
	attrs.products = products
	attrs.selectedProductId = selectedProductId
	attrs.onProductSelect = onProductSelect
}
