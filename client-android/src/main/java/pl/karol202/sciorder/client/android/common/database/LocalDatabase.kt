package pl.karol202.sciorder.client.android.common.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import pl.karol202.sciorder.client.android.common.database.converter.OrderStatusConverter
import pl.karol202.sciorder.client.android.common.database.converter.ProductParameterTypeConverter
import pl.karol202.sciorder.client.android.common.database.converter.StringListConverter
import pl.karol202.sciorder.client.android.common.database.dao.OrderEntityDao
import pl.karol202.sciorder.client.android.common.database.dao.OwnerEntityDao
import pl.karol202.sciorder.client.android.common.database.dao.ProductEntityDao
import pl.karol202.sciorder.client.android.common.database.entity.OrderEntity
import pl.karol202.sciorder.client.android.common.database.entity.OwnerEntity
import pl.karol202.sciorder.client.android.common.database.entity.ProductEntity

private object DatabaseInfo
{
	const val NAME = "local"
	const val VERSION = 6
}

@Database(entities = [ProductEntity::class, OrderEntity::class, OwnerEntity::class],
          version = DatabaseInfo.VERSION,
          exportSchema = false)
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
