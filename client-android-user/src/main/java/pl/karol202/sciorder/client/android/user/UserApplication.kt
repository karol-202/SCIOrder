package pl.karol202.sciorder.client.android.user

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.karol202.sciorder.client.android.common.SCIOrderApplication
import pl.karol202.sciorder.client.android.user.viewmodel.OrderComposeViewModel
import pl.karol202.sciorder.client.android.user.viewmodel.OrdersTrackViewModel
import pl.karol202.sciorder.client.android.user.viewmodel.OwnerViewModel
import pl.karol202.sciorder.client.android.user.viewmodel.ProductsViewModel

class UserApplication : SCIOrderApplication()
{
	override val modules = listOf(viewModelsModule())

	private fun viewModelsModule() = module {
		viewModel { OwnerViewModel(get(), get()) }
		viewModel { ProductsViewModel(get(), get()) }
		viewModel { OrderComposeViewModel(get(), get()) }
		viewModel { OrdersTrackViewModel(get(), get()) }
	}
}
