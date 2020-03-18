package pl.karol202.sciorder.client.android.common.database.room.relations

import androidx.room.Embedded
import androidx.room.Relation
import pl.karol202.sciorder.client.android.common.database.room.entity.OrderEntryEntity
import pl.karol202.sciorder.client.android.common.database.room.entity.OrderEntryParameterValueEntity
import pl.karol202.sciorder.client.android.common.util.*
import pl.karol202.sciorder.common.model.OrderEntry

data class OrderEntryWithParameters(@Embedded val orderEntry: OrderEntryEntity,
                                    @Relation(entity = OrderEntryParameterValueEntity::class,
                                              parentColumn = "id",
                                              entityColumn = "orderEntryId")
                                    val parameters: List<OrderEntryParameterValueEntity>)
{
    companion object : ToModelMapper<OrderEntryWithParameters, OrderEntry>,
                       ToEntityMapper<OrderEntryWithParameters, OrderEntry>
    {
        override fun toModel(entity: OrderEntryWithParameters) = with(entity) {
            OrderEntry(orderEntry.id, orderEntry.orderId, orderEntry.productId, orderEntry.quantity,
                       parameters.toModels(OrderEntryParameterValueEntity).toMap())
        }
    
        override fun toEntity(model: OrderEntry) =
                OrderEntryWithParameters(model.toEntity(OrderEntryEntity),
                                         model.parameters.toEntities(OrderEntryParameterValueEntity.mapper(model.id)))
    }
}

val List<OrderEntryWithParameters>.entries get() = map { it.orderEntry }
val List<OrderEntryWithParameters>.parameterValues get() = flatMap { it.parameters }
