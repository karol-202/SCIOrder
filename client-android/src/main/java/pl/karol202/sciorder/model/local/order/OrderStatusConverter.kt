package pl.karol202.sciorder.model.local.order

import androidx.room.TypeConverter
import pl.karol202.sciorder.model.Order

class OrderStatusConverter
{
	@TypeConverter
	fun fromStatus(status: Order.Status) = status.ordinal

	@TypeConverter
	fun toStatus(ordinal: Int) = Order.Status.values()[ordinal]
}
