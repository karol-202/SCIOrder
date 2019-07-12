package pl.karol202.sciorder.client.js.common.view

import pl.karol202.sciorder.client.common.model.remote.createApiHttpClient
import pl.karol202.sciorder.client.common.model.remote.order.KtorOrderApi
import pl.karol202.sciorder.client.common.model.remote.owner.KtorOwnerApi
import pl.karol202.sciorder.client.common.model.remote.product.KtorProductApi
import pl.karol202.sciorder.client.common.repository.order.OrderRepositoryImpl
import pl.karol202.sciorder.client.common.repository.ordertrack.OrderTrackRepositoryImpl
import pl.karol202.sciorder.client.common.repository.owner.OwnerRepositoryImpl
import pl.karol202.sciorder.client.common.repository.product.ProductRepositoryImpl
import pl.karol202.sciorder.client.js.common.model.local.FakeOrderDao
import pl.karol202.sciorder.client.js.common.model.local.FakeOwnerDao
import pl.karol202.sciorder.client.js.common.model.local.FakeProductDao
import pl.karol202.sciorder.client.js.common.viewmodel.OrderComposeJsViewModel
import pl.karol202.sciorder.client.js.common.viewmodel.OrdersJsViewModel
import pl.karol202.sciorder.client.js.common.viewmodel.OrdersTrackJsViewModel
import pl.karol202.sciorder.client.js.common.viewmodel.OwnerJsViewModel
import pl.karol202.sciorder.client.js.common.viewmodel.ProductsJsViewModel
import pl.karol202.sciorder.client.js.common.viewmodel.ViewModels
import react.RBuilder
import react.RProps
import react.RState
import react.router.dom.browserRouter

class AppView : View<RProps, AppView.State>()
{
	interface State : RState
	{
		var viewModels: ViewModels
	}

	init
	{
		val httpClient = createApiHttpClient()
		val ownerApi = KtorOwnerApi(httpClient)
		val productApi = KtorProductApi(httpClient)
		val orderApi = KtorOrderApi(httpClient)

		val ownerDao = FakeOwnerDao()
		val productDao = FakeProductDao()
		val orderDao = FakeOrderDao()

		val ownerRepository = OwnerRepositoryImpl(ownerDao, ownerApi)
		val productRepository = ProductRepositoryImpl(productDao, productApi)
		val orderRepository = OrderRepositoryImpl(orderDao, orderApi)
		val orderTrackRepository = OrderTrackRepositoryImpl(orderDao, orderApi)

		state.viewModels = ViewModels(OwnerJsViewModel(ownerRepository, orderDao, productDao),
		                        ProductsJsViewModel(ownerRepository, productRepository),
		                        OrderComposeJsViewModel(ownerRepository, orderTrackRepository),
		                        OrdersJsViewModel(ownerRepository, orderRepository),
		                        OrdersTrackJsViewModel(ownerRepository, orderTrackRepository))
	}

	override fun RBuilder.render()
	{
		themeComponent {
			browserRouter {
				mainView(state.viewModels)
			}
		}
	}
}

fun RBuilder.appComponent() = child(AppView::class) { }
