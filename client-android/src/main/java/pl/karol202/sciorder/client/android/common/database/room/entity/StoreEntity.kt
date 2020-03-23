package pl.karol202.sciorder.client.android.common.database.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import pl.karol202.sciorder.client.android.common.database.room.ToEntityMapper
import pl.karol202.sciorder.client.android.common.database.room.ToModelMapper
import pl.karol202.sciorder.common.model.Store
import pl.karol202.sciorder.common.util.IdProvider

@Entity
data class StoreEntity(@PrimaryKey override val id: Long,
                       val name: String,
                       val selected: Boolean) : IdProvider<Long>
{
	companion object : ToEntityMapper<StoreEntity, Store>, ToModelMapper<StoreEntity, Store>
	{
		override fun toEntity(model: Store) = StoreEntity(model.id, model.name, false)
		
		override fun toModel(entity: StoreEntity) = Store(entity.id, entity.name)
	}
}
