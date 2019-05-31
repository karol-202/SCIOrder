package pl.karol202.sciorder.client.admin

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.karol202.sciorder.client.admin.viewmodel.OrdersViewModel
import pl.karol202.sciorder.client.admin.viewmodel.OwnerViewModelAdmin
import pl.karol202.sciorder.client.admin.viewmodel.ProductsViewModel
import pl.karol202.sciorder.client.common.SCIOrderApplication
import pl.karol202.sciorder.client.common.viewmodel.OwnerViewModel

class SCIOrderApplicationAdmin : SCIOrderApplication()
{
	override val modules = listOf(viewModelsModule())

	private fun viewModelsModule() = module {
		viewModel { OwnerViewModelAdmin(get(), get(), get(), get()) as OwnerViewModel }
		viewModel { ProductsViewModel(get(), get(), get(), get()) }
		viewModel { OrdersViewModel(get(), get(), get(), get()) }
	}
}
