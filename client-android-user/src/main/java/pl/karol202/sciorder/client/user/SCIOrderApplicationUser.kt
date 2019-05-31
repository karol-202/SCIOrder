package pl.karol202.sciorder.client.user

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.karol202.sciorder.client.common.SCIOrderApplication
import pl.karol202.sciorder.client.common.viewmodel.OwnerViewModel
import pl.karol202.sciorder.client.user.viewmodel.OrdersTrackViewModel
import pl.karol202.sciorder.client.user.viewmodel.OrdersViewModel
import pl.karol202.sciorder.client.user.viewmodel.OwnerViewModelUser
import pl.karol202.sciorder.client.user.viewmodel.ProductsViewModel

class SCIOrderApplicationUser : SCIOrderApplication()
{
	override val modules = listOf(viewModelsModule())

	private fun viewModelsModule() = module {
		viewModel { OwnerViewModelUser(get(), get(), get()) as OwnerViewModel }
		viewModel { ProductsViewModel(get(), get(), get(), get()) }
		viewModel { OrdersViewModel(get(), get(), get(), get()) }
		viewModel { OrdersTrackViewModel(get(), get(), get(), get()) }
	}
}
