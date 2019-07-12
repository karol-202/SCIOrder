package pl.karol202.sciorder.client.js.common.view

import com.ccfraser.muirwik.components.MTypographyColor
import com.ccfraser.muirwik.components.list.mList
import com.ccfraser.muirwik.components.list.mListItem
import com.ccfraser.muirwik.components.list.mListItemText
import com.ccfraser.muirwik.components.mTypography
import pl.karol202.sciorder.client.js.common.util.prop
import pl.karol202.sciorder.client.js.common.viewmodel.ProductsJsViewModel
import pl.karol202.sciorder.common.Product
import react.RBuilder
import react.RProps
import react.RState
import react.buildElement
import react.setState

class UserProductsView(props: Props) : View<UserProductsView.Props, UserProductsView.State>(props)
{
	interface Props : RProps
	{
		var productsViewModel: ProductsJsViewModel?
	}

	interface State : RState
	{
		var products: List<Product>
		var selectedProductId: String?
	}

	private val productsViewModel by prop { productsViewModel }

	init
	{
		state.products = emptyList()

		productsViewModel.productsObservable.bindToState { products = it ?: emptyList() }
	}

	override fun RBuilder.render()
	{
		productsList()
		productOrderView()
	}

	private fun RBuilder.productsList() = mList {
		state.products.forEach { product(it) }
	}

	private fun RBuilder.product(product: Product) = mListItem(button = product.available,
	                                                           selected = product.id == state.selectedProductId,
	                                                           onClick = { selectProductIfAvailable(product) }) {
		mListItemText(primary = createProductName(product), disableTypography = true)
	}

	private fun createProductName(product: Product) = buildElement {
		mTypography(product.name, color = if(product.available) MTypographyColor.textPrimary else MTypographyColor.textSecondary)
	}

	private fun RBuilder.productOrderView() =
			state.products.singleOrNull { it.id == state.selectedProductId }?.let { productOrderView(it, {}, {}) }

	private fun selectProductIfAvailable(product: Product)
	{
		if(product.available) setState { selectedProductId = product.id }
	}
}

fun RBuilder.userProductsView(productsViewModel: ProductsJsViewModel) = child(UserProductsView::class) {
	attrs.productsViewModel = productsViewModel
}
