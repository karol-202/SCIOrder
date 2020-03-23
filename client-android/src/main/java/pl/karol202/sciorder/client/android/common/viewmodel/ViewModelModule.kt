package pl.karol202.sciorder.client.android.common.viewmodel

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun viewModelsModule() = module {
	viewModel { AdminLoginAndroidViewModel(get(), get(), get(), get(), get()) }
	viewModel { AdminOrdersAndroidViewModel(get(), get(), get()) }
	viewModel { AdminProductsAndroidViewModel(get(), get(), get()) }
	viewModel { AdminStoresAndroidViewModel(get(), get()) }
	viewModel { UserLoginAndroidViewModel(get(), get(), get(), get(), get()) }
	viewModel { UserOrderComposeAndroidViewModel(get(), get()) }
	viewModel { UserOrdersAndroidViewModel(get(), get()) }
	viewModel { UserProductsAndroidViewModel(get(), get()) }
}
