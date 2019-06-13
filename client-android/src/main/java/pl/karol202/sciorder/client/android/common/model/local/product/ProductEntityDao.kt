package pl.karol202.sciorder.client.android.common.model.local.product

import androidx.room.*

@Dao
interface ProductEntityDao
{
	@Insert
	suspend fun insert(products: List<ProductEntity>)

	@Update
	suspend fun update(products: List<ProductEntity>)

	@Delete
	suspend fun delete(products: List<ProductEntity>)

	@Query("DELETE FROM ${ProductEntity.TABLE_NAME}")
	suspend fun deleteAll()

    @Query("SELECT * FROM ${ProductEntity.TABLE_NAME}")
    suspend fun getAll(): List<ProductEntity>
}
