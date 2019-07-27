package pl.karol202.sciorder.client.js.common.view

import pl.karol202.sciorder.client.common.model.remote.createApiHttpClient
import pl.karol202.sciorder.client.common.model.remote.order.KtorOrderApi
import pl.karol202.sciorder.client.common.model.remote.owner.KtorOwnerApi
import pl.karol202.sciorder.client.common.model.remote.product.KtorProductApi
import pl.karol202.sciorder.client.common.repository.order.OrderRepositoryImpl
import pl.karol202.sciorder.client.common.repository.ordertrack.OrderTrackRepositoryImpl
import pl.karol202.sciorder.client.common.repository.owner.OwnerRepositoryImpl
import pl.karol202.sciorder.client.common.repository.product.ProductRepositoryImpl
import pl.karol202.sciorder.client.js.common.model.local.LocalOrderDao
import pl.karol202.sciorder.client.js.common.model.local.LocalOwnerDao
import pl.karol202.sciorder.client.js.common.model.local.LocalProductDao
import pl.karol202.sciorder.client.js.common.viewmodel.*
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

		val ownerDao = LocalOwnerDao()
		val productDao = LocalProductDao()
		val orderDao = LocalOrderDao()

		val ownerRepository = OwnerRepositoryImpl(ownerDao, ownerApi)
		val productRepository = ProductRepositoryImpl(productDao, productApi)
		val orderRepository = OrderRepositoryImpl(orderDao, orderApi)
		val orderTrackRepository = OrderTrackRepositoryImpl(orderDao, orderApi)

		state.viewModels = ViewModels(OwnerJsViewModel(ownerRepository, orderDao, productDao),
		                              ProductsJsViewModel(ownerRepository, productRepository),
		                              ProductsEditJsViewModel(ownerRepository, productRepository),
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
