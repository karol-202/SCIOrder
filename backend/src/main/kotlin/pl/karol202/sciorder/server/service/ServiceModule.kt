package pl.karol202.sciorder.server.service

import org.koin.dsl.module
import pl.karol202.sciorder.server.service.admin.AdminService
import pl.karol202.sciorder.server.service.admin.AdminServiceImpl
import pl.karol202.sciorder.server.service.order.OrderService
import pl.karol202.sciorder.server.service.order.OrderServiceImpl
import pl.karol202.sciorder.server.service.permission.PermissionService
import pl.karol202.sciorder.server.service.permission.PermissionServiceImpl
import pl.karol202.sciorder.server.service.product.ProductService
import pl.karol202.sciorder.server.service.product.ProductServiceImpl
import pl.karol202.sciorder.server.service.product.parameter.ProductParameterService
import pl.karol202.sciorder.server.service.product.parameter.ProductParameterServiceImpl
import pl.karol202.sciorder.server.service.store.StoreService
import pl.karol202.sciorder.server.service.store.StoreServiceImpl
import pl.karol202.sciorder.server.service.user.UserService
import pl.karol202.sciorder.server.service.user.UserServiceImpl

fun serviceModule() = module {
	single<PermissionService> { PermissionServiceImpl() }
	single<ProductService> { ProductServiceImpl() }
	single<ProductParameterService> { ProductParameterServiceImpl() }
	single<OrderService> { OrderServiceImpl() }
	single<StoreService> { StoreServiceImpl() }
	single<AdminService> { AdminServiceImpl(get(), get()) }
	single<UserService> { UserServiceImpl(get()) }
}
