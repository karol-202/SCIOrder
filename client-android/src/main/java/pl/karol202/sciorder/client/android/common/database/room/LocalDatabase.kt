package pl.karol202.sciorder.client.android.common.database.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import pl.karol202.sciorder.client.android.common.database.room.converter.OrderStatusConverter
import pl.karol202.sciorder.client.android.common.database.room.converter.ProductParameterTypeConverter
import pl.karol202.sciorder.client.android.common.database.room.dao.*
import pl.karol202.sciorder.client.android.common.database.room.entity.*

private object DatabaseInfo
{
	const val NAME = "sciorder.local"
	const val VERSION = 1
}

@Database(entities =
          [
	          StoreEntity::class,
	          ProductEntity::class, ProductParameterEntity::class, ProductParameterEnumValueEntity::class,
	          OrderEntity::class, OrderEntryEntity::class, OrderEntryParameterValueEntity::class
          ],
          version = DatabaseInfo.VERSION)
@TypeConverters(OrderStatusConverter::class, ProductParameterTypeConverter::class)
abstract class LocalDatabase : RoomDatabase()
{
	companion object
	{
		fun create(context: Context) =
				Room.databaseBuilder(context, LocalDatabase::class.java, DatabaseInfo.NAME)
						.fallbackToDestructiveMigration() // TODO Remove it before deployment
						.build()
	}
	
	abstract fun storeEntityDao(): StoreEntityDao

	abstract fun productEntityDao(): ProductEntityDao

	abstract fun productParameterEntityDao(): ProductParameterEntityDao
	
	abstract fun productParameterEnumValueEntityDao(): ProductParameterEnumValueEntityDao
	
	abstract fun orderEntityDao(): OrderEntityDao

	abstract fun orderEntryEntityDao(): OrderEntryEntityDao
	
	abstract fun orderEntryParameterValueEntityDao(): OrderEntryParameterValueEntityDao
}
