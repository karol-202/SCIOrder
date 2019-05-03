package pl.karol202.sciorder.model.local.order

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import pl.karol202.sciorder.model.Order

@Entity(tableName = OrderEntity.TABLE_NAME)
data class OrderEntity(@PrimaryKey val id: String,
                       val entries: List<Order.Entry>,
                       @Embedded val details: Order.Details,
                       val status: Order.Status)
{
    companion object
    {
        const val TABLE_NAME = "orders"
    }
}
