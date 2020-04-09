package pl.karol202.sciorder.client.android.common.database.room.relations

import androidx.room.Embedded
import androidx.room.Relation
import pl.karol202.sciorder.client.android.common.database.room.*
import pl.karol202.sciorder.client.android.common.database.room.entity.ProductParameterEntity
import pl.karol202.sciorder.client.android.common.database.room.entity.ProductParameterEnumValueEntity
import pl.karol202.sciorder.common.model.ProductParameter

data class ProductParameterWithEnumValues(@Embedded val parameter: ProductParameterEntity,
                                          @Relation(entity = ProductParameterEnumValueEntity::class,
                                                    parentColumn = "id",
                                                    entityColumn = "productParameterId")
                                          val enumValues: List<ProductParameterEnumValueEntity>)
{
	companion object : ToModelMapper<ProductParameterWithEnumValues, ProductParameter>,
	                   ToEntityMapper<ProductParameterWithEnumValues, ProductParameter>
	{
		override fun toEntity(model: ProductParameter) = ProductParameterWithEnumValues(
				model.toEntity(ProductParameterEntity),
				model.attributes.enumValues?.toEntities(ProductParameterEnumValueEntity.mapper(model.id)).orEmpty()
		)
		
		override fun toModel(entity: ProductParameterWithEnumValues) = with(entity) {
			ProductParameter(parameter.id, parameter.productId, parameter.name, parameter.type,
			                 ProductParameter.Attributes(minimalValue = parameter.minimalValue,
			                                             maximalValue = parameter.maximalValue,
			                                             defaultValue = parameter.defaultValue,
			                                             enumValues = enumValues.toModels(ProductParameterEnumValueEntity)))
		}
	}
}

val List<ProductParameterWithEnumValues>.parameters get() = map { it.parameter }
val List<ProductParameterWithEnumValues>.enumValues get() = flatMap { it.enumValues }
