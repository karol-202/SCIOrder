package pl.karol202.sciorder.client.android.common.database.room.entity

import androidx.room.*
import pl.karol202.sciorder.common.model.Order
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
data class OrderEntity(@PrimaryKey override val id: Long,
                       val storeId: Long,
                       @Embedded val details: Order.Details,
                       val status: Order.Status) : IdProvider<Long>
