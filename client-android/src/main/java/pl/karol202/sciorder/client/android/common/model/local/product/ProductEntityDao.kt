package pl.karol202.sciorder.client.android.common.model.local.product

import androidx.room.*
import io.reactivex.Flowable

@Dao
interface ProductEntityDao
{
	@Insert(onConflict = OnConflictStrategy.IGNORE)
	suspend fun insert(products: List<ProductEntity>)

	@Update
	suspend fun update(products: List<ProductEntity>)

	@Delete
	suspend fun delete(products: List<ProductEntity>)

	@Query("DELETE FROM ${ProductEntity.TABLE_NAME}")
	suspend fun deleteAll()

    @Query("SELECT * FROM ${ProductEntity.TABLE_NAME}")
    fun getAll(): Flowable<List<ProductEntity>>
	
	@Query("SELECT * FROM ${ProductEntity.TABLE_NAME} WHERE id = :id")
	fun getById(id: String): Flowable<List<ProductEntity>>
}
