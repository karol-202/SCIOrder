package pl.karol202.sciorder.client.common.model.local.product

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ProductEntityDao
{
	@WorkerThread
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insert(products: List<ProductEntity>)

	@WorkerThread
	@Update
	fun update(products: List<ProductEntity>)

	@WorkerThread
	@Delete
	fun delete(products: List<ProductEntity>)

    @Query("SELECT * FROM ${ProductEntity.TABLE_NAME}")
    fun getAll(): LiveData<List<ProductEntity>>
}
