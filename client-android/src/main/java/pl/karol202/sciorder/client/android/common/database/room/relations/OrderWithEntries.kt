package pl.karol202.sciorder.client.android.common.database.room.relations

import androidx.room.Embedded
import androidx.room.Relation
import pl.karol202.sciorder.client.android.common.database.room.entity.OrderEntity
import pl.karol202.sciorder.client.android.common.database.room.entity.OrderEntryEntity
import pl.karol202.sciorder.common.model.Order
import pl.karol202.sciorder.common.util.Mappable

data class OrderWithEntries(@Embedded val order: OrderEntity,
                            @Relation(entity = OrderEntryEntity::class,
                                      parentColumn = "id",
                                      entityColumn = "orderId")
                            val entries: List<OrderEntryWithParameters>) : Mappable<Order>
{
    override fun map() = Order(order.id, order.storeId, entries.map { it.map() }, order.details, order.status)
}
