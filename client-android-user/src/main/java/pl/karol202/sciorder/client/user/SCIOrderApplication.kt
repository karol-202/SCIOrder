package pl.karol202.sciorder.client.user

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import pl.karol202.sciorder.client.common.KoinCommon
import pl.karol202.sciorder.client.user.viewmodel.OrdersTrackViewModel
import pl.karol202.sciorder.client.user.viewmodel.OrdersViewModel
import pl.karol202.sciorder.client.user.viewmodel.ProductsViewModel

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
		viewModel { ProductsViewModel(get(), get()) }
		viewModel { OrdersViewModel(get(), get()) }
		viewModel { OrdersTrackViewModel(get(), get()) }
	}
}
