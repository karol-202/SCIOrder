package pl.karol202.sciorder.client.android.common.database.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import pl.karol202.sciorder.client.android.common.database.room.ToEntityMapper
import pl.karol202.sciorder.client.android.common.database.room.ToModelMapper
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
		fun mapper(productParameterId: Long) = ToEntityMapper<ProductParameterEnumValueEntity, String> {
			ProductParameterEnumValueEntity(0, productParameterId, it)
		}
		
		override fun toModel(entity: ProductParameterEnumValueEntity) = entity.value
	}
}
