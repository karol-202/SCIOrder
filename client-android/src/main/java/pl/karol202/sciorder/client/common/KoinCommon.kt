package pl.karol202.sciorder.client.common

import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import pl.karol202.sciorder.client.common.model.local.LocalDatabase
import pl.karol202.sciorder.client.common.model.local.order.toOrderDao
import pl.karol202.sciorder.client.common.model.local.product.toProductDao
import pl.karol202.sciorder.client.common.model.remote.LiveDataCallAdapterFactory
import pl.karol202.sciorder.client.common.model.remote.order.OrderApi
import pl.karol202.sciorder.client.common.model.remote.product.ProductApi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

object KoinCommon
{
	private const val SERVER_URL = "https://sciorder.herokuapp.com"

	fun loadModules() = loadKoinModules(databaseModule(),
	                                    networkingModule())

	private fun databaseModule() = module {
		single { LocalDatabase.create(androidContext()) }

		single { get<LocalDatabase>().productEntityDao().toProductDao() }
		single { get<LocalDatabase>().orderEntityDao().toOrderDao() }
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
}
