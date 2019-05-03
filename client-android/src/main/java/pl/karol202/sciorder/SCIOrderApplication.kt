package pl.karol202.sciorder

import android.app.Application
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import pl.karol202.sciorder.model.local.LocalDatabase
import pl.karol202.sciorder.model.remote.LiveDataCallAdapterFactory
import pl.karol202.sciorder.model.remote.order.OrderApi
import pl.karol202.sciorder.model.remote.product.ProductApi
import pl.karol202.sciorder.viewmodel.OrderViewModel
import pl.karol202.sciorder.viewmodel.ProductViewModel
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

class SCIOrderApplication : Application()
{
	companion object
	{
		private const val SERVER_URL = "http://192.168.0.12:8080/"
	}

	override fun onCreate()
	{
		super.onCreate()
		startKoin {
			androidContext(this@SCIOrderApplication)
			modules(databaseModule(),
			        networkingModule(),
			        viewModelsModule())
		}
	}

	private fun databaseModule() = module {
		single { LocalDatabase.create(androidContext()) }
	}

	private fun networkingModule() = module {
		fun okHttp() = OkHttpClient.Builder()
			.connectTimeout(3, TimeUnit.SECONDS)
			.readTimeout(3, TimeUnit.SECONDS)
			.build()

		fun retrofit(httpClient: OkHttpClient) = Retrofit.Builder()
			.baseUrl(SERVER_URL)
			.client(httpClient)
			.addConverterFactory(MoshiConverterFactory.create())
			.addCallAdapterFactory(LiveDataCallAdapterFactory())
			.build()

		single { okHttp() }
		single { retrofit(get()) }

		single { get<Retrofit>().create<ProductApi>() }
		single { get<Retrofit>().create<OrderApi>() }
	}

	private fun viewModelsModule() = module {
		viewModel { ProductViewModel(get(), get()) }
		viewModel { OrderViewModel(get()) }
	}
}
