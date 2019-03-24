package pl.karol202.sciorder.model.local

import androidx.lifecycle.LiveData
import androidx.room.*
import pl.karol202.sciorder.model.Product

@Dao
@TypeConverters(ProductParametersListConverter::class)
interface ProductDao
{
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertProducts(products: List<Product>)

    @Query("SELECT * FROM ${ProductEntity.TABLE_NAME}")
    fun getAllProducts(): LiveData<List<Product>>
}