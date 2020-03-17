package pl.karol202.sciorder.client.android.common.database.room.relations

import androidx.room.Embedded
import androidx.room.Relation
import pl.karol202.sciorder.client.android.common.database.room.entity.OrderEntryEntity
import pl.karol202.sciorder.client.android.common.database.room.entity.OrderEntryParameterValueEntity
import pl.karol202.sciorder.common.model.OrderEntry
import pl.karol202.sciorder.common.util.Mappable

data class OrderEntryWithParameters(@Embedded val orderEntry: OrderEntryEntity,
                                    @Relation(entity = OrderEntryParameterValueEntity::class,
                                              parentColumn = "id",
                                              entityColumn = "orderEntryId")
                                    val parameters: List<OrderEntryParameterValueEntity>) : Mappable<OrderEntry>
{
    private val parametersMap get() = parameters.associate { it.productParameterId to it.value }
    
    override fun map() =
            OrderEntry(orderEntry.id, orderEntry.orderId, orderEntry.productId, orderEntry.quantity, parametersMap)
}
