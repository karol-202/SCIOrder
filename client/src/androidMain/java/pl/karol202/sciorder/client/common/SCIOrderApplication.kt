package pl.karol202.sciorder.client.common

import android.app.Application
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module
import pl.karol202.sciorder.client.common.model.local.LocalDatabase
import pl.karol202.sciorder.client.common.model.local.order.toOrderDao
import pl.karol202.sciorder.client.common.model.local.owner.toOwnerDao
import pl.karol202.sciorder.client.common.model.local.product.toProductDao
import pl.karol202.sciorder.client.common.model.remote.ApiResponseCallAdapter
import pl.karol202.sciorder.client.common.model.remote.OrderApi
import pl.karol202.sciorder.client.common.model.remote.OwnerApi
import pl.karol202.sciorder.client.common.model.remote.ProductApi
import pl.karol202.sciorder.client.common.repository.order.OrderRepository
import pl.karol202.sciorder.client.common.repository.order.OrderRepositoryImpl
import pl.karol202.sciorder.client.common.repository.ordertrack.OrderTrackRepository
import pl.karol202.sciorder.client.common.repository.ordertrack.OrderTrackRepositoryImpl
import pl.karol202.sciorder.client.common.repository.owner.OwnerRepository
import pl.karol202.sciorder.client.common.repository.owner.OwnerRepositoryImpl
import pl.karol202.sciorder.client.common.repository.product.ProductRepository
import pl.karol202.sciorder.client.common.repository.product.ProductRepositoryImpl
import retrofit2.Retrofit
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
				.addCallAdapterFactory(ApiResponseCallAdapter.Factory())
				.build()

		single { okHttp() }
		single { retrofit(get()) }

		single { get<Retrofit>().create<OwnerApi>() }
		single { get<Retrofit>().create<ProductApi>() }
		single { get<Retrofit>().create<OrderApi>() }
	}

	private fun repositoryModule() = module {
		single<OrderRepository> { OrderRepositoryImpl(get(), get()) }
		single<OrderTrackRepository> { OrderTrackRepositoryImpl(get(), get()) }
		single<OwnerRepository> { OwnerRepositoryImpl(get(), get()) }
		single<ProductRepository> { ProductRepositoryImpl(get(), get()) }
	}
}
