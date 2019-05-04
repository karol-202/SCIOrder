package pl.karol202.sciorder.client.user.model.local.product

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ProductEntityDao
{
	@WorkerThread
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertProducts(products: List<ProductEntity>)

	@WorkerThread
	@Query("DELETE FROM ${ProductEntity.TABLE_NAME}")
	fun deleteProducts()

    @Query("SELECT * FROM ${ProductEntity.TABLE_NAME}")
    fun getAllProducts(): LiveData<List<ProductEntity>>
}
