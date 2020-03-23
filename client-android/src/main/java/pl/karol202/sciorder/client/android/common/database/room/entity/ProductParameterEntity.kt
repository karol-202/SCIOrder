package pl.karol202.sciorder.client.android.common.database.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import pl.karol202.sciorder.client.android.common.database.room.ToEntityMapper
import pl.karol202.sciorder.common.model.ProductParameter
import pl.karol202.sciorder.common.util.IdProvider

@Entity(foreignKeys =
        [
			ForeignKey(entity = ProductEntity::class,
			           parentColumns = [ "id" ],
			           childColumns = [ "productId" ],
			           onUpdate = ForeignKey.CASCADE,
			           onDelete = ForeignKey.CASCADE)
		],
        indices = [Index("productId")])
data class ProductParameterEntity(@PrimaryKey override val id: Long,
                                  val productId: Long,
                                  val name: String,
                                  val type: ProductParameter.Type,
                                  val minimalValue: Float?,
                                  val maximalValue: Float?,
                                  val defaultValue: String?) : IdProvider<Long>
{
	companion object : ToEntityMapper<ProductParameterEntity, ProductParameter>
	{
		override fun toEntity(model: ProductParameter) = with(model) {
			ProductParameterEntity(id, productId, name, type,
			                       attributes.minimalValue, attributes.maximalValue, attributes.defaultValue)
		}
	}
}
