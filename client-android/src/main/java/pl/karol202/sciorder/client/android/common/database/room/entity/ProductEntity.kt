package pl.karol202.sciorder.client.android.common.database.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import pl.karol202.sciorder.client.android.common.util.EntityModelMapper
import pl.karol202.sciorder.common.model.Product
import pl.karol202.sciorder.common.util.IdProvider

@Entity(foreignKeys = [
	ForeignKey(entity = StoreEntity::class,
	           parentColumns = [ "id" ],
	           childColumns = [ "storeId" ],
	           onUpdate = ForeignKey.CASCADE,
	           onDelete = ForeignKey.CASCADE)
])
data class ProductEntity(@PrimaryKey override val id: Long,
                         val storeId: Long,
                         val name: String,
                         val available: Boolean) : IdProvider
{
	companion object : EntityModelMapper<ProductEntity, Product>
	{
		override fun toModel(entity: ProductEntity) =
				Product(entity.id, entity.storeId, entity.name, entity.available, emptyList())
		
		override fun toEntity(model: Product) =
				ProductEntity(model.id, model.storeId, model.name, model.available)
	}
}
