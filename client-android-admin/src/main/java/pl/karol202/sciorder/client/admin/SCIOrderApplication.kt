package pl.karol202.sciorder.client.admin

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import pl.karol202.sciorder.client.admin.viewmodel.OrdersViewModel
import pl.karol202.sciorder.client.admin.viewmodel.OwnerViewModel
import pl.karol202.sciorder.client.admin.viewmodel.ProductsViewModel
import pl.karol202.sciorder.client.common.KoinCommon

class SCIOrderApplication : Application()
{
	override fun onCreate()
	{
		super.onCreate()
		startKoin {
			androidContext(this@SCIOrderApplication)
			modules(viewModelsModule())
		}
		KoinCommon.loadModules()
	}

	private fun viewModelsModule() = module {
		viewModel { OwnerViewModel(get(), get()) }
		viewModel { ProductsViewModel(get(), get(), get()) }
		viewModel { OrdersViewModel(get(), get(), get()) }
	}
}
