package pl.karol202.sciorder.server.controller

import org.koin.dsl.module
import pl.karol202.sciorder.server.controller.admin.AdminController
import pl.karol202.sciorder.server.controller.admin.AdminControllerImpl
import pl.karol202.sciorder.server.controller.order.OrderController
import pl.karol202.sciorder.server.controller.order.OrderControllerImpl
import pl.karol202.sciorder.server.controller.product.ProductController
import pl.karol202.sciorder.server.controller.product.ProductControllerImpl
import pl.karol202.sciorder.server.controller.product.parameter.ProductParameterController
import pl.karol202.sciorder.server.controller.product.parameter.ProductParameterControllerImpl
import pl.karol202.sciorder.server.controller.store.StoreController
import pl.karol202.sciorder.server.controller.store.StoreControllerImpl
import pl.karol202.sciorder.server.controller.user.UserController
import pl.karol202.sciorder.server.controller.user.UserControllerImpl

fun controllerModule() = module {
	single<ProductController> { ProductControllerImpl(get(), get()) }
	single<ProductParameterController> { ProductParameterControllerImpl(get(), get()) }
	single<OrderController> { OrderControllerImpl(get(), get(), get()) }
	single<StoreController> { StoreControllerImpl(get(), get()) }
	single<AdminController> { AdminControllerImpl(get(), get()) }
	single<UserController> { UserControllerImpl(get()) }
}
