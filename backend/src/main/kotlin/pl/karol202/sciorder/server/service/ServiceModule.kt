package pl.karol202.sciorder.server.service

import org.koin.core.qualifier.named
import org.koin.dsl.module
import pl.karol202.sciorder.server.config.Config
import pl.karol202.sciorder.server.config.getProperty
import pl.karol202.sciorder.server.service.admin.AdminService
import pl.karol202.sciorder.server.service.admin.AdminServiceImpl
import pl.karol202.sciorder.server.service.admin.TransactionAdminService
import pl.karol202.sciorder.server.service.hash.HashService
import pl.karol202.sciorder.server.service.hash.SHA256HashService
import pl.karol202.sciorder.server.service.order.OrderService
import pl.karol202.sciorder.server.service.order.OrderServiceImpl
import pl.karol202.sciorder.server.service.order.TransactionOrderService
import pl.karol202.sciorder.server.service.permission.PermissionService
import pl.karol202.sciorder.server.service.permission.PermissionServiceImpl
import pl.karol202.sciorder.server.service.product.ProductService
import pl.karol202.sciorder.server.service.product.ProductServiceImpl
import pl.karol202.sciorder.server.service.product.TransactionProductService
import pl.karol202.sciorder.server.service.product.parameter.ProductParameterService
import pl.karol202.sciorder.server.service.product.parameter.ProductParameterServiceImpl
import pl.karol202.sciorder.server.service.product.parameter.TransactionProductParameterService
import pl.karol202.sciorder.server.service.store.StoreService
import pl.karol202.sciorder.server.service.store.StoreServiceImpl
import pl.karol202.sciorder.server.service.store.TransactionStoreService
import pl.karol202.sciorder.server.service.storeadmin.StoreAdminService
import pl.karol202.sciorder.server.service.storeadmin.StoreAdminServiceImpl
import pl.karol202.sciorder.server.service.storeadmin.TransactionStoreAdminService
import pl.karol202.sciorder.server.service.transaction.TransactionService
import pl.karol202.sciorder.server.service.transaction.TransactionServiceImpl
import pl.karol202.sciorder.server.service.user.TransactionUserService
import pl.karol202.sciorder.server.service.user.UserService
import pl.karol202.sciorder.server.service.user.UserServiceImpl

private val NO_TRANSACTION = named("no_transaction")

fun serviceModule() = module {
	single<PermissionService> { PermissionServiceImpl(get()) }
	single<TransactionService> { TransactionServiceImpl(dbUri = getProperty(Config.DB_URI),
	                                                    dbDriver = getProperty(Config.DB_DRIVER),
	                                                    dbUser = getProperty(Config.DB_USER),
	                                                    dbPassword = getProperty(Config.DB_PASSWORD)) }
	single<HashService> { SHA256HashService() }
	
	single<ProductService>(NO_TRANSACTION) { ProductServiceImpl(get(NO_TRANSACTION)) }
	single<ProductParameterService>(NO_TRANSACTION) { ProductParameterServiceImpl() }
	single<OrderService>(NO_TRANSACTION) { OrderServiceImpl() }
	single<StoreService>(NO_TRANSACTION) { StoreServiceImpl() }
	single<StoreAdminService>(NO_TRANSACTION) { StoreAdminServiceImpl() }
	single<AdminService>(NO_TRANSACTION) { AdminServiceImpl(get(), get()) }
	single<UserService>(NO_TRANSACTION) { UserServiceImpl(get(), get()) }
	
	single<ProductService> { TransactionProductService(get(NO_TRANSACTION), get()) }
	single<ProductParameterService> { TransactionProductParameterService(get(NO_TRANSACTION), get()) }
	single<OrderService> { TransactionOrderService(get(NO_TRANSACTION), get()) }
	single<StoreService> { TransactionStoreService(get(NO_TRANSACTION), get()) }
	single<StoreAdminService> { TransactionStoreAdminService(get(NO_TRANSACTION), get()) }
	single<AdminService> { TransactionAdminService(get(NO_TRANSACTION), get()) }
	single<UserService> { TransactionUserService(get(NO_TRANSACTION), get()) }
}
