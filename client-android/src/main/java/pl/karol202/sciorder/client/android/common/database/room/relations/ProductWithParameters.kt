package pl.karol202.sciorder.client.android.common.database.room.relations

import androidx.room.Embedded
import androidx.room.Relation
import pl.karol202.sciorder.client.android.common.database.room.entity.ProductEntity
import pl.karol202.sciorder.client.android.common.database.room.entity.ProductParameterEntity
import pl.karol202.sciorder.client.android.common.util.*
import pl.karol202.sciorder.common.model.Product

data class ProductWithParameters(@Embedded val product: ProductEntity,
                                 @Relation(entity = ProductParameterEntity::class,
                                           parentColumn = "id",
                                           entityColumn = "productId")
                                 val parameters: List<ProductParameterWithEnumValues>)
{
	companion object : ToModelMapper<ProductWithParameters, Product>, ToEntityMapper<ProductWithParameters, Product>
	{
		override fun toEntity(model: Product) =
				ProductWithParameters(model.toEntity(ProductEntity),
				                      model.parameters.toEntities(ProductParameterWithEnumValues))
		
		override fun toModel(entity: ProductWithParameters) = with(entity) {
			Product(product.id, product.storeId, product.name, product.available,
			        parameters.toModels(ProductParameterWithEnumValues))
		}
	}
}

val List<ProductWithParameters>.products get() = map { it.product }
val List<ProductWithParameters>.parameters get() = flatMap { it.parameters }.parameters
val List<ProductWithParameters>.enumValues get() = flatMap { it.parameters }.enumValues
