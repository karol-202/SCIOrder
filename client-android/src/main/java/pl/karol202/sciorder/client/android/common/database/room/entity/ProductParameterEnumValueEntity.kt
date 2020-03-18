package pl.karol202.sciorder.client.android.common.database.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import pl.karol202.sciorder.client.android.common.util.ToEntityMapper
import pl.karol202.sciorder.client.android.common.util.ToModelMapper
import pl.karol202.sciorder.common.util.IdProvider

@Entity(foreignKeys =
        [
			ForeignKey(entity = ProductParameterEntity::class,
			           parentColumns = [ "id" ],
			           childColumns = [ "productParameterId" ],
			           onUpdate = ForeignKey.CASCADE,
			           onDelete = ForeignKey.CASCADE)
		],
        indices = [Index("productParameterId")])
data class ProductParameterEnumValueEntity(@PrimaryKey(autoGenerate = true) override val id: Long,
                                           val productParameterId: Long,
                                           val value: String) : IdProvider<Long>
{
	companion object : ToModelMapper<ProductParameterEnumValueEntity, String>
	{
		fun mapper(productParameterId: Long) = object : ToEntityMapper<ProductParameterEnumValueEntity, String>
		{
			override fun toEntity(model: String) = ProductParameterEnumValueEntity(0, productParameterId, model)
		}
		
		override fun toModel(entity: ProductParameterEnumValueEntity) = entity.value
	}
}
