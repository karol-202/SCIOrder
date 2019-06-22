package pl.karol202.sciorder.client.android.common

import android.app.Application
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module
import pl.karol202.sciorder.client.android.common.model.local.LocalDatabase
import pl.karol202.sciorder.client.android.common.model.local.order.toOrderDao
import pl.karol202.sciorder.client.android.common.model.local.owner.toOwnerDao
import pl.karol202.sciorder.client.android.common.model.local.product.toProductDao
import pl.karol202.sciorder.client.android.common.model.remote.*
import pl.karol202.sciorder.client.common.repository.order.OrderRepository
import pl.karol202.sciorder.client.common.repository.order.OrderRepositoryImpl
import pl.karol202.sciorder.client.common.repository.ordertrack.OrderTrackRepository
import pl.karol202.sciorder.client.common.repository.ordertrack.OrderTrackRepositoryImpl
import pl.karol202.sciorder.client.common.repository.owner.OwnerRepository
import pl.karol202.sciorder.client.common.repository.owner.OwnerRepositoryImpl
import pl.karol202.sciorder.client.common.repository.product.ProductRepository
import pl.karol202.sciorder.client.common.repository.product.ProductRepositoryImpl
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

abstract class SCIOrderApplication : Application()
{
	companion object
	{
		private const val SERVER_URL = "https://sciorder.herokuapp.com"
	}

	abstract val modules: List<Module>

	override fun onCreate()
	{
		super.onCreate()
		startKoin {
			androidContext(this@SCIOrderApplication)
			modules(databaseModule() + networkingModule() + repositoryModule() + modules)
		}
	}

	private fun databaseModule() = module {
		single { LocalDatabase.create(androidContext()) }

		single { get<LocalDatabase>().productEntityDao().toProductDao() }
		single { get<LocalDatabase>().orderEntityDao().toOrderDao() }
		single { get<LocalDatabase>().ownerEntityDao().toOwnerDao() }
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
				.addCallAdapterFactory(ApiResponseCallAdapter.Factory())
				.build()

		single { okHttp() }
		single { retrofit(get()) }

		single { get<Retrofit>().create<RetrofitOwnerApi>().asOwnerApi() }
		single { get<Retrofit>().create<RetrofitProductApi>().asProductApi() }
		single { get<Retrofit>().create<RetrofitOrderApi>().asOrderApi() }
	}

	private fun repositoryModule() = module {
		single<OrderRepository> { OrderRepositoryImpl(get(), get()) }
		single<OrderTrackRepository> { OrderTrackRepositoryImpl(get(), get()) }
		single<OwnerRepository> { OwnerRepositoryImpl(get(), get()) }
		single<ProductRepository> { ProductRepositoryImpl(get(), get()) }
	}
}
