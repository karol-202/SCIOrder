package pl.karol202.sciorder.server

import org.koin.dsl.module
import pl.karol202.sciorder.server.dao.OrderDao
import pl.karol202.sciorder.server.dao.ProductDao
import pl.karol202.sciorder.server.dao.impl.OrderDaoImpl
import pl.karol202.sciorder.server.dao.impl.ProductDaoImpl

object KoinModules
{
	fun databaseModule() = module {
		single { KMongoDatabase(getProperty(ARG_MONGODB)) }
		single<OwnerDao> { DatabaseOwnerDao(get()) }
		single<ProductDao> { ProductDaoImpl(get()) }
		single<OrderDao> { OrderDaoImpl(get()) }
	}
}
