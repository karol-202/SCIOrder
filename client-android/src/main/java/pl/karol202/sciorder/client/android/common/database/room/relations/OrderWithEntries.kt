package pl.karol202.sciorder.client.android.common.database.room.relations

import androidx.room.Embedded
import androidx.room.Relation
import pl.karol202.sciorder.client.android.common.database.room.*
import pl.karol202.sciorder.client.android.common.database.room.entity.OrderEntity
import pl.karol202.sciorder.client.android.common.database.room.entity.OrderEntryEntity
import pl.karol202.sciorder.common.model.Order

data class OrderWithEntries(@Embedded val order: OrderEntity,
                            @Relation(entity = OrderEntryEntity::class,
                                      parentColumn = "id",
                                      entityColumn = "orderId")
                            val entries: List<OrderEntryWithParameters>)
{
    companion object : ToModelMapper<OrderWithEntries, Order>, ToEntityMapper<OrderWithEntries, Order>
    {
        override fun toModel(entity: OrderWithEntries) = with(entity) {
            Order(order.id, order.storeId, entries.toModels(OrderEntryWithParameters), order.details, order.status)
        }
    
        override fun toEntity(model: Order) =
                OrderWithEntries(model.toEntity(OrderEntity), model.entries.toEntities(OrderEntryWithParameters))
    }
}

val List<OrderWithEntries>.orders get() = map { it.order }
val List<OrderWithEntries>.entries get() = flatMap { it.entries }.entries
val List<OrderWithEntries>.parameterValues get() = flatMap { it.entries }.parameterValues
