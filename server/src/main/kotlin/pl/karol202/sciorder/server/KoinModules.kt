package pl.karol202.sciorder.server

import org.koin.dsl.module
import pl.karol202.sciorder.server.database.*

object KoinModules
{
	fun databaseModule() = module {
		single { KMongoDatabase(getProperty(ARG_MONGODB)) }
		single<ProductDao> { DatabaseProductDao(get()) }
		single<OrderDao> { DatabaseOrderDao(get()) }
	}
}
