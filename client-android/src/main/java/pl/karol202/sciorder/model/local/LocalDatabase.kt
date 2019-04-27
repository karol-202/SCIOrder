package pl.karol202.sciorder.model.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import pl.karol202.sciorder.model.local.product.ProductEntity
import pl.karol202.sciorder.model.local.product.ProductEntityDao
import pl.karol202.sciorder.model.local.product.ProductParametersListConverter

private object DatabaseInfo
{
	const val NAME = "local"
	const val VERSION = 1
}

@Database(entities = [ProductEntity::class], version = DatabaseInfo.VERSION)
@TypeConverters(ProductParametersListConverter::class)
abstract class LocalDatabase : RoomDatabase()
{
	companion object
	{
		fun create(context: Context) = Room.databaseBuilder(context, LocalDatabase::class.java, DatabaseInfo.NAME).build()
	}

	abstract fun productEntityDao(): ProductEntityDao
}