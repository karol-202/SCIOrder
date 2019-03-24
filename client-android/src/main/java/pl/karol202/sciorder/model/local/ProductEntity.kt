package pl.karol202.sciorder.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import pl.karol202.sciorder.model.Product

@Entity(tableName = ProductEntity.TABLE_NAME)
data class ProductEntity(@PrimaryKey val id: String,
                         val name: String,
                         val available: Boolean,
                         val parameters: List<Product.Parameter>)
{
    companion object
    {
    	const val TABLE_NAME = "products"
    }
}