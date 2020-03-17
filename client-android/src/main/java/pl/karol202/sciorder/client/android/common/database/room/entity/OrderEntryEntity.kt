package pl.karol202.sciorder.client.android.common.database.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import pl.karol202.sciorder.common.model.OrderEntry
import pl.karol202.sciorder.common.util.IdProvider

@Entity(foreignKeys = [
	ForeignKey(entity = OrderEntry::class,
	           parentColumns = [ "id" ],
	           childColumns = [ "orderId" ],
	           onUpdate = ForeignKey.CASCADE,
	           onDelete = ForeignKey.CASCADE),
	ForeignKey(entity = ProductEntity::class,
	           parentColumns = [ "id" ],
	           childColumns = [ "productId" ],
	           onUpdate = ForeignKey.CASCADE,
	           onDelete = ForeignKey.CASCADE)
])
data class OrderEntryEntity(@PrimaryKey override val id: Long,
                            val orderId: Long,
                            val productId: Long,
                            val quantity: Int) : IdProvider<Long>
