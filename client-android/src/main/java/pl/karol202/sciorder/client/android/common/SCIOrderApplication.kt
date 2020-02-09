package pl.karol202.sciorder.client.android.common

import android.app.Application
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module
import pl.karol202.sciorder.client.android.common.database.LocalDatabase
import pl.karol202.sciorder.client.android.common.database.order.toOrderDao
import pl.karol202.sciorder.client.android.common.database.owner.toOwnerDao
import pl.karol202.sciorder.client.android.common.database.product.toProductDao
import pl.karol202.sciorder.client.common.api.createApiHttpClient
import pl.karol202.sciorder.client.common.api.order.KtorOrderApi
import pl.karol202.sciorder.client.common.api.order.OrderApi
import pl.karol202.sciorder.client.common.api.owner.KtorOwnerApi
import pl.karol202.sciorder.client.common.api.owner.OwnerApi
import pl.karol202.sciorder.client.common.api.product.KtorProductApi
import pl.karol202.sciorder.client.common.api.product.ProductApi
import pl.karol202.sciorder.client.common.repository.order.OrderRepository
import pl.karol202.sciorder.client.common.repository.order.OrderRepositoryImpl
import pl.karol202.sciorder.client.common.repository.ordertrack.OrderTrackRepository
import pl.karol202.sciorder.client.common.repository.ordertrack.OrderTrackRepositoryImpl
import pl.karol202.sciorder.client.common.repository.owner.OwnerRepository
import pl.karol202.sciorder.client.common.repository.owner.OwnerRepositoryImpl
import pl.karol202.sciorder.client.common.repository.product.ProductRepository
import pl.karol202.sciorder.client.common.repository.product.ProductRepositoryImpl

abstract class SCIOrderApplication : Application()
{
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
		single { createApiHttpClient(OkHttp) }

		single<OwnerApi> { KtorOwnerApi(get()) }
		single<ProductApi> { KtorProductApi(get()) }
		single<OrderApi> { KtorOrderApi(get()) }
	}

	private fun repositoryModule() = module {
		single<OrderRepository> { OrderRepositoryImpl(get(), get()) }
		single<OrderTrackRepository> { OrderTrackRepositoryImpl(get(), get()) }
		single<OwnerRepository> { OwnerRepositoryImpl(get(), get()) }
		single<ProductRepository> { ProductRepositoryImpl(get(), get()) }
	}
}
