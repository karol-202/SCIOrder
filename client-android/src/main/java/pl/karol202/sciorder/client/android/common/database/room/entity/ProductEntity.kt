package pl.karol202.sciorder.client.android.common.database.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import pl.karol202.sciorder.common.util.IdProvider

@Entity(foreignKeys =
        [
			ForeignKey(entity = StoreEntity::class,
			           parentColumns = [ "id" ],
			           childColumns = [ "storeId" ],
			           onUpdate = ForeignKey.CASCADE,
			           onDelete = ForeignKey.CASCADE)
		],
        indices = [Index("storeId")])
data class ProductEntity(@PrimaryKey override val id: Long,
                         val storeId: Long,
                         val name: String,
                         val available: Boolean) : IdProvider<Long>
