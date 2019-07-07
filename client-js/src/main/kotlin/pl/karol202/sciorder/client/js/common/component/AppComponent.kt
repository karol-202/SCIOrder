package pl.karol202.sciorder.client.js.common.component

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
import pl.karol202.sciorder.client.js.common.util.ExtendedComponent
import pl.karol202.sciorder.client.js.common.view.mainView
import pl.karol202.sciorder.client.js.common.viewmodel.*
import react.RBuilder
import react.RProps
import react.RState
import react.router.dom.browserRouter

class AppComponent : ExtendedComponent<RProps, RState>()
{
	private val httpClient = createApiHttpClient()
	private val ownerApi = KtorOwnerApi(httpClient)
	private val productApi = KtorProductApi(httpClient)
	private val orderApi = KtorOrderApi(httpClient)

	private val ownerDao = FakeOwnerDao()
	private val productDao = FakeProductDao()
	private val orderDao = FakeOrderDao()

	private val ownerRepository = OwnerRepositoryImpl(ownerDao, ownerApi)
	private val productRepository = ProductRepositoryImpl(productDao, productApi)
	private val orderRepository = OrderRepositoryImpl(orderDao, orderApi)
	private val orderTrackRepository = OrderTrackRepositoryImpl(orderDao, orderApi)

	private val viewModels = ViewModels(OwnerJsViewModel(ownerRepository, orderDao, productDao),
	                                    ProductJsViewModel(ownerRepository, productRepository),
	                                    OrderComposeJsViewModel(ownerRepository, orderTrackRepository),
	                                    OrdersJsViewModel(ownerRepository, orderRepository),
	                                    OrdersTrackJsViewModel(ownerRepository, orderTrackRepository))

	override fun RBuilder.render()
	{
		themeComponent {
			browserRouter {
				mainView(viewModels)
			}
		}
	}
}

fun RBuilder.appComponent() = child(AppComponent::class) { }
