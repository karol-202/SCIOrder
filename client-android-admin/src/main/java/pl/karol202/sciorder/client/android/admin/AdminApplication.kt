package pl.karol202.sciorder.client.android.admin

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.karol202.sciorder.client.android.admin.viewmodel.AdminOwnerAndroidViewModel
import pl.karol202.sciorder.client.android.common.SCIOrderApplication
import pl.karol202.sciorder.client.android.common.viewmodel.OrdersAndroidViewModel
import pl.karol202.sciorder.client.android.common.viewmodel.OwnerAndroidViewModel
import pl.karol202.sciorder.client.android.common.viewmodel.ProductsAndroidViewModel

class AdminApplication : SCIOrderApplication()
{
	override val modules = listOf(viewModelsModule())

	private fun viewModelsModule() = module {
		viewModel<OwnerAndroidViewModel> { AdminOwnerAndroidViewModel(get(), get(), get()) }
		viewModel { ProductsAndroidViewModel(get(), get()) }
		viewModel { OrdersAndroidViewModel(get(), get()) }
	}
}
