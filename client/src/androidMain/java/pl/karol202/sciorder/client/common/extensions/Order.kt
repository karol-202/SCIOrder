@file:JvmName("OrderAndroidKt")

package pl.karol202.sciorder.client.common.extensions

import pl.karol202.sciorder.client.common.R
import pl.karol202.sciorder.common.Order

val Order.Status.visibleName get() = when(this)
{
	Order.Status.WAITING -> R.string.order_status_waiting
	Order.Status.IN_PROGRESS -> R.string.order_status_in_progress
	Order.Status.DELIVERING -> R.string.order_status_delivering
	Order.Status.DONE -> R.string.order_status_done
	Order.Status.REJECTED -> R.string.order_status_rejected
}

val Order.Status.color get() = when(this)
{
	Order.Status.WAITING -> R.color.order_status_waiting
	Order.Status.IN_PROGRESS -> R.color.order_status_in_progress
	Order.Status.DELIVERING -> R.color.order_status_delivering
	Order.Status.DONE -> R.color.order_status_done
	Order.Status.REJECTED -> R.color.order_status_rejected
}
