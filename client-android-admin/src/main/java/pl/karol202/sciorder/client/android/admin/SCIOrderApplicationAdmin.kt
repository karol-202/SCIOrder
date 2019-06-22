package pl.karol202.sciorder.client.android.admin

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.karol202.sciorder.client.android.admin.viewmodel.OrdersViewModel
import pl.karol202.sciorder.client.android.admin.viewmodel.OwnerViewModelAdmin
import pl.karol202.sciorder.client.android.admin.viewmodel.ProductsViewModel
import pl.karol202.sciorder.client.android.common.SCIOrderApplication
import pl.karol202.sciorder.client.android.common.viewmodel.OwnerViewModel

class SCIOrderApplicationAdmin : SCIOrderApplication()
{
	override val modules = listOf(viewModelsModule())

	private fun viewModelsModule() = module {
		viewModel { OwnerViewModelAdmin(get(), get(), get()) as OwnerViewModel }
		viewModel { ProductsViewModel(get(), get()) }
		viewModel { OrdersViewModel(get(), get()) }
	}
}
