package pl.karol202.sciorder.client.android.user

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.karol202.sciorder.client.android.user.viewmodel.OrdersTrackViewModel
import pl.karol202.sciorder.client.android.user.viewmodel.OrdersViewModel
import pl.karol202.sciorder.client.android.user.viewmodel.OwnerViewModelUser
import pl.karol202.sciorder.client.android.user.viewmodel.ProductsViewModel
import pl.karol202.sciorder.client.common.SCIOrderApplication
import pl.karol202.sciorder.client.common.viewmodel.OwnerViewModel

class SCIOrderApplicationUser : SCIOrderApplication()
{
	override val modules = listOf(viewModelsModule())

	private fun viewModelsModule() = module {
		viewModel { OwnerViewModelUser(get(), get()) as OwnerViewModel }
		viewModel { ProductsViewModel(get(), get()) }
		viewModel { OrdersViewModel(get(), get()) }
		viewModel { OrdersTrackViewModel(get(), get()) }
	}
}
