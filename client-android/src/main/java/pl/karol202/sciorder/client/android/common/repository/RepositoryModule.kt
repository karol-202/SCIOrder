package pl.karol202.sciorder.client.android.common.repository

import org.koin.dsl.module
import pl.karol202.sciorder.client.common.repository.admin.AdminRepository
import pl.karol202.sciorder.client.common.repository.admin.AdminRepositoryImpl
import pl.karol202.sciorder.client.common.repository.auth.admin.AdminAuthRepository
import pl.karol202.sciorder.client.common.repository.auth.admin.AdminAuthRepositoryImpl
import pl.karol202.sciorder.client.common.repository.auth.user.UserAuthRepository
import pl.karol202.sciorder.client.common.repository.auth.user.UserAuthRepositoryImpl
import pl.karol202.sciorder.client.common.repository.order.OrderRepository
import pl.karol202.sciorder.client.common.repository.order.OrderRepositoryImpl
import pl.karol202.sciorder.client.common.repository.product.ProductRepository
import pl.karol202.sciorder.client.common.repository.product.ProductRepositoryImpl
import pl.karol202.sciorder.client.common.repository.store.StoreRepository
import pl.karol202.sciorder.client.common.repository.store.StoreRepositoryImpl
import pl.karol202.sciorder.client.common.repository.user.UserRepository
import pl.karol202.sciorder.client.common.repository.user.UserRepositoryImpl

fun repositoryModule() = module {
	single<AdminRepository> { AdminRepositoryImpl(get()) }
	single<AdminAuthRepository> { AdminAuthRepositoryImpl(get(), get()) }
	single<UserAuthRepository> { UserAuthRepositoryImpl(get(), get()) }
	single<OrderRepository> { OrderRepositoryImpl(get(), get()) }
	single<ProductRepository> { ProductRepositoryImpl(get(), get()) }
	single<StoreRepository> { StoreRepositoryImpl(get(), get()) }
	single<UserRepository> { UserRepositoryImpl(get(), get()) }
}
