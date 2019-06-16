package pl.karol202.sciorder.client.android.admin

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.karol202.sciorder.client.android.admin.viewmodel.OrdersViewModel
import pl.karol202.sciorder.client.android.admin.viewmodel.OwnerViewModelAdmin
import pl.karol202.sciorder.client.android.admin.viewmodel.ProductsViewModel
import pl.karol202.sciorder.client.common.SCIOrderApplication

class SCIOrderApplicationAdmin : SCIOrderApplication()
{
	override val modules = listOf(viewModelsModule())

	private fun viewModelsModule() = module {
		viewModel { OwnerViewModelAdmin(get(), get(), get()) }
		viewModel { ProductsViewModel(get(), get()) }
		viewModel { OrdersViewModel(get(), get()) }
	}
}
