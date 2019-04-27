package pl.karol202.sciorder.model.local.product

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ProductEntityDao
{
	@WorkerThread
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertProducts(products: List<ProductEntity>)

    @Query("SELECT * FROM ${ProductEntity.TABLE_NAME}")
    fun getAllProducts(): LiveData<List<ProductEntity>>
}