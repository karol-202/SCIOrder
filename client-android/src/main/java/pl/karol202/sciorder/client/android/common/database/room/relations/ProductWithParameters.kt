package pl.karol202.sciorder.client.android.common.database.room.relations

import androidx.room.Embedded
import androidx.room.Relation
import pl.karol202.sciorder.client.android.common.database.room.entity.ProductEntity
import pl.karol202.sciorder.client.android.common.database.room.entity.ProductParameterEntity
import pl.karol202.sciorder.common.model.Product
import pl.karol202.sciorder.common.util.Mappable
import pl.karol202.sciorder.common.util.map

data class ProductWithParameters(@Embedded val product: ProductEntity,
                                 @Relation(entity = ProductParameterEntity::class,
                                           parentColumn = "id",
                                           entityColumn = "productId")
                                 val parameters: List<ProductParameterWithEnumValues>) : Mappable<Product>
{
	override fun map() = Product(product.id, product.storeId, product.name, product.available, parameters.map())
}
