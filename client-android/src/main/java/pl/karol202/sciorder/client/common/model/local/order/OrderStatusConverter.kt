package pl.karol202.sciorder.client.common.model.local.order

import androidx.room.TypeConverter
import pl.karol202.sciorder.common.model.Order

class OrderStatusConverter
{
	@TypeConverter
	fun fromStatus(status: Order.Status) = status.ordinal

	@TypeConverter
	fun toStatus(ordinal: Int) = Order.Status.values()[ordinal]
}
