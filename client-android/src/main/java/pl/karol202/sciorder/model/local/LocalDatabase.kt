package pl.karol202.sciorder.model.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import pl.karol202.sciorder.model.local.order.OrderEntity
import pl.karol202.sciorder.model.local.order.OrderEntityDao
import pl.karol202.sciorder.model.local.order.OrderEntriesListConverter
import pl.karol202.sciorder.model.local.order.OrderStatusConverter
import pl.karol202.sciorder.model.local.product.ProductEntity
import pl.karol202.sciorder.model.local.product.ProductEntityDao
import pl.karol202.sciorder.model.local.product.ProductParametersListConverter

private object DatabaseInfo
{
	const val NAME = "local"
	const val VERSION = 2
}

@Database(entities = [ProductEntity::class, OrderEntity::class], version = DatabaseInfo.VERSION)
@TypeConverters(ProductParametersListConverter::class, OrderEntriesListConverter::class, OrderStatusConverter::class)
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
}
