package pl.karol202.sciorder.client.android.common.database

import androidx.preference.PreferenceManager
import com.tfcporciuncula.flow.FlowSharedPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import pl.karol202.sciorder.client.android.common.database.dao.*
import pl.karol202.sciorder.client.android.common.database.room.LocalDatabase
import pl.karol202.sciorder.client.common.database.dao.*

fun databaseModule() = module {
	single { LocalDatabase.create(androidContext()) }
	
	single { get<LocalDatabase>().storeEntityDao() }
	single { get<LocalDatabase>().productEntityDao() }
	single { get<LocalDatabase>().productParameterEntityDao() }
	single { get<LocalDatabase>().productParameterEnumValueEntityDao() }
	single { get<LocalDatabase>().orderEntityDao() }
	single { get<LocalDatabase>().orderEntryEntityDao() }
	single { get<LocalDatabase>().orderEntryParameterValueEntityDao() }
	
	single { PreferenceManager.getDefaultSharedPreferences(androidContext()) }
	single { FlowSharedPreferences(get()) }
	
	single<AdminAuthDao> { AdminAuthDaoImpl(get()) }
	single<OrderDao> { OrderDaoImpl(get(), get(), get(), get()) }
	single<ProductDao> { ProductDaoImpl(get(), get(), get(), get()) }
	single<StoreDao> { StoreDaoImpl(get(), get(), get(), get(), get(), get(), get(), get()) }
	single<UserAuthDao> { UserAuthDaoImpl(get()) }
	single<UserDao> { UserDaoImpl(get()) }
}
