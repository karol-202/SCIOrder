package pl.karol202.sciorder.server.service

import org.koin.dsl.module
import pl.karol202.sciorder.server.service.admin.AdminService
import pl.karol202.sciorder.server.service.admin.AdminServiceImpl
import pl.karol202.sciorder.server.service.admin.TransactionAdminService
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
import pl.karol202.sciorder.server.service.transaction.TransactionService
import pl.karol202.sciorder.server.service.transaction.TransactionServiceImpl
import pl.karol202.sciorder.server.service.user.TransactionUserService
import pl.karol202.sciorder.server.service.user.UserService
import pl.karol202.sciorder.server.service.user.UserServiceImpl

fun serviceModule() = module {
	single<PermissionService> { PermissionServiceImpl() }
	single<TransactionService> { TransactionServiceImpl() }
	single<ProductService> { TransactionProductService(ProductServiceImpl(), get()) }
	single<ProductParameterService> { TransactionProductParameterService(ProductParameterServiceImpl(), get()) }
	single<OrderService> { TransactionOrderService(OrderServiceImpl(), get()) }
	single<StoreService> { TransactionStoreService(StoreServiceImpl(), get()) }
	single<AdminService> { TransactionAdminService(AdminServiceImpl(get(), get()), get()) }
	single<UserService> { TransactionUserService(UserServiceImpl(get()), get()) }
}
