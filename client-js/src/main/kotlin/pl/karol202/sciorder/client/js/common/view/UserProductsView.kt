package pl.karol202.sciorder.client.js.common.view

import com.ccfraser.muirwik.components.MTypographyColor
import com.ccfraser.muirwik.components.list.mList
import com.ccfraser.muirwik.components.list.mListItem
import com.ccfraser.muirwik.components.list.mListItemText
import com.ccfraser.muirwik.components.mTypography
import pl.karol202.sciorder.client.js.common.util.ExtendedComponent
import pl.karol202.sciorder.client.js.common.util.prop
import pl.karol202.sciorder.client.js.common.viewmodel.ProductsJsViewModel
import pl.karol202.sciorder.common.Product
import react.RBuilder
import react.RProps
import react.RState
import react.buildElement

class UserProductsView : ExtendedComponent<UserProductsView.Props, UserProductsView.State>()
{
	interface Props : RProps
	{
		var productsViewModel: ProductsJsViewModel?
	}

	interface State : RState
	{
		var products: List<Product>
	}

	private val productsViewModel by prop { productsViewModel }

	override fun State.init()
	{
		products = emptyList()
	}

	override fun componentDidMount()
	{
		productsViewModel.productsObservable.bindToState { products = it ?: emptyList() }
	}

	override fun RBuilder.render()
	{
		mList {
			state.products.forEach { product(it) }
		}
	}

	private fun RBuilder.product(product: Product) = mListItem(button = product.available) {
		mListItemText(primary = createProductName(product), disableTypography = true)
	}

	private fun createProductName(product: Product) = buildElement {
		mTypography(product.name, color = if(product.available) MTypographyColor.textPrimary else MTypographyColor.textSecondary)
	}
}

fun RBuilder.userProductsView(productsViewModel: ProductsJsViewModel) = child(UserProductsView::class) {
	attrs.productsViewModel = productsViewModel
}
