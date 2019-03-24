package pl.karol202.sciorder.model.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query

@Dao
interface ProductDao
{
    @Query("SELECT * FROM ${ProductEntity.TABLE_NAME}")
    fun getAllProdcts(): LiveData<List<ProductEntity>>
}