package pl.karol202.sciorder.client.android.common.database.room.relations

import androidx.room.Embedded
import androidx.room.Relation
import pl.karol202.sciorder.client.android.common.database.room.entity.ProductParameterEntity
import pl.karol202.sciorder.client.android.common.database.room.entity.ProductParameterEnumValueEntity
import pl.karol202.sciorder.common.model.ProductParameter
import pl.karol202.sciorder.common.util.Mappable

data class ProductParameterWithEnumValues(@Embedded val parameter: ProductParameterEntity,
                                          @Relation(entity = ProductParameterEnumValueEntity::class,
                                                    parentColumn = "id",
                                                    entityColumn = "productParameterId")
                                          val enumValues: List<ProductParameterEnumValueEntity>) : Mappable<ProductParameter>
{
	private val enumValuesList get() = enumValues.map { it.value }
	
	override fun map() =
			ProductParameter(parameter.id, parameter.productId, parameter.name, parameter.type,
			                 ProductParameter.Attributes(minimalValue = parameter.minimalValue,
			                                             maximalValue = parameter.maximalValue,
			                                             defaultValue = parameter.defaultValue,
			                                             enumValues = enumValuesList))
}
