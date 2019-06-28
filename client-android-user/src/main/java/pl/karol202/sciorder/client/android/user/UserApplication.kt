package pl.karol202.sciorder.client.android.user

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.karol202.sciorder.client.android.common.SCIOrderApplication
import pl.karol202.sciorder.client.android.common.viewmodel.OrderComposeAndroidViewModel
import pl.karol202.sciorder.client.android.common.viewmodel.OrdersTrackAndroidViewModel
import pl.karol202.sciorder.client.android.common.viewmodel.OwnerAndroidViewModel
import pl.karol202.sciorder.client.android.common.viewmodel.ProductsAndroidViewModel
import pl.karol202.sciorder.client.android.user.viewmodel.UserOwnerAndroidViewModel

class UserApplication : SCIOrderApplication()
{
	override val modules = listOf(viewModelsModule())

	private fun viewModelsModule() = module {
		viewModel<OwnerAndroidViewModel> { UserOwnerAndroidViewModel(get(), get()) }
		viewModel { ProductsAndroidViewModel(get(), get()) }
		viewModel { OrderComposeAndroidViewModel(get(), get()) }
		viewModel { OrdersTrackAndroidViewModel(get(), get()) }
	}
}
