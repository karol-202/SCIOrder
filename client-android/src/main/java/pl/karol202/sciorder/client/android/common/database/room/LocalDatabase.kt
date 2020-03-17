package pl.karol202.sciorder.client.android.common.database.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import pl.karol202.sciorder.client.android.common.database.room.converter.OrderStatusConverter
import pl.karol202.sciorder.client.android.common.database.room.converter.ProductParameterTypeConverter
import pl.karol202.sciorder.client.android.common.database.room.entity.OrderEntity
import pl.karol202.sciorder.client.android.common.database.room.entity.ProductEntity

private object DatabaseInfo
{
	const val NAME = "sciorder.local"
	const val VERSION = 1
}

@Database(entities = [ProductEntity::class, OrderEntity::class, OwnerEntity::class],
          version = DatabaseInfo.VERSION)
@TypeConverters(StringListConverter::class, OrderStatusConverter::class, ProductParameterTypeConverter::class)
abstract class LocalDatabase : RoomDatabase()
{
	companion object
	{
		fun create(context: Context) =
				Room.databaseBuilder(context, LocalDatabase::class.java, DatabaseInfo.NAME)
						.fallbackToDestructiveMigration()
						.build()
	}

	abstract fun productEntityDao(): ProductEntityDao

	abstract fun orderEntityDao(): OrderEntityDao

	abstract fun ownerEntityDao(): OwnerEntityDao
}
