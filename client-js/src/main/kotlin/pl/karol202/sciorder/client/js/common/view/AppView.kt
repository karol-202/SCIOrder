package pl.karol202.sciorder.client.js.common.view

import pl.karol202.sciorder.client.common.model.remote.createApiHttpClient
import pl.karol202.sciorder.client.common.model.remote.order.KtorOrderApi
import pl.karol202.sciorder.client.common.model.remote.owner.KtorOwnerApi
import pl.karol202.sciorder.client.common.model.remote.product.KtorProductApi
import pl.karol202.sciorder.client.common.repository.order.OrderRepositoryImpl
import pl.karol202.sciorder.client.common.repository.ordertrack.OrderTrackRepositoryImpl
import pl.karol202.sciorder.client.common.repository.owner.OwnerRepositoryImpl
import pl.karol202.sciorder.client.common.repository.product.ProductRepositoryImpl
import pl.karol202.sciorder.client.js.common.model.local.LocalDao
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

		val ownerAdminDao = LocalOwnerDao(LocalDao.STORAGE_ADMIN_OWNER)
		val ownerUserDao = LocalOwnerDao(LocalDao.STORAGE_USER_OWNER)
		val productAdminDao = LocalProductDao(LocalDao.STORAGE_ADMIN_PRODUCTS)
		val productUserDao = LocalProductDao(LocalDao.STORAGE_USER_PRODUCTS)
		val orderAdminDao = LocalOrderDao(LocalDao.STORAGE_ADMIN_ORDERS)
		val orderUserDao = LocalOrderDao(LocalDao.STORAGE_USER_ORDERS)

		val ownerAdminRepository = OwnerRepositoryImpl(ownerAdminDao, ownerApi)
		val ownerUserRepository = OwnerRepositoryImpl(ownerUserDao, ownerApi)
		val productAdminRepository = ProductRepositoryImpl(productAdminDao, productApi)
		val productUserRepository = ProductRepositoryImpl(productUserDao, productApi)
		val orderAdminRepository = OrderRepositoryImpl(orderAdminDao, orderApi)
		val orderTrackUserRepository = OrderTrackRepositoryImpl(orderUserDao, orderApi)

		state.viewModels = ViewModels(AdminOwnerJsViewModel(ownerAdminRepository, orderAdminDao, productAdminDao),
		                              UserOwnerJsViewModel(ownerUserRepository, productUserDao),
		                              ProductsJsViewModel(ownerUserRepository, productUserRepository),
		                              ProductsEditJsViewModel(ownerAdminRepository, productAdminRepository),
		                              OrderComposeJsViewModel(ownerUserRepository, orderTrackUserRepository),
		                              OrdersJsViewModel(ownerAdminRepository, orderAdminRepository),
		                              OrdersTrackJsViewModel(ownerUserRepository, orderTrackUserRepository))
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
