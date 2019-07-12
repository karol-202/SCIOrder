package pl.karol202.sciorder.client.js.common.view

import pl.karol202.sciorder.client.js.common.util.prop
import pl.karol202.sciorder.client.js.common.viewmodel.OrdersTrackJsViewModel
import pl.karol202.sciorder.client.js.common.viewmodel.ProductsJsViewModel
import react.RBuilder
import react.RProps
import react.RState

class UserView(props: Props) : View<UserView.Props, RState>(props)
{
	interface Props : RProps
	{
		var productsViewModel: ProductsJsViewModel?
		var ordersTrackViewModel: OrdersTrackJsViewModel?
	}

	private val productsViewModel by prop { productsViewModel }
	private val ordersTrackViewModel by prop { ordersTrackViewModel }

	override fun RBuilder.render()
	{
		userProductsView(productsViewModel)
	}
}

fun RBuilder.userView(productsViewModel: ProductsJsViewModel, ordersTrackViewModel: OrdersTrackJsViewModel) =
		child(UserView::class) {
			attrs.productsViewModel = productsViewModel
			attrs.ordersTrackViewModel = ordersTrackViewModel
		}
