package pl.karol202.sciorder.server

import org.koin.dsl.module
import pl.karol202.sciorder.server.dao.OrderDao
import pl.karol202.sciorder.server.dao.OwnerDao
import pl.karol202.sciorder.server.dao.ProductDao
import pl.karol202.sciorder.server.dao.impl.DatabaseOwnerDao
import pl.karol202.sciorder.server.dao.impl.DatabaseProductDao
import pl.karol202.sciorder.server.dao.impl.OrderDaoImpl

object KoinModules
{
	fun databaseModule() = module {
		single { KMongoDatabase(getProperty(ARG_MONGODB)) }
		single<OwnerDao> { DatabaseOwnerDao(get()) }
		single<ProductDao> { DatabaseProductDao(get()) }
		single<OrderDao> { OrderDaoImpl(get()) }
	}
}
