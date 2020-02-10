package pl.karol202.sciorder.client.android.common.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import pl.karol202.sciorder.common.model.Order

@Entity
data class OrderEntity(@PrimaryKey val id: String,
                       val ownerId: String,
                       val entries: List<Order.Entry>,
                       @Embedded val details: Order.Details,
                       val status: Order.Status)
