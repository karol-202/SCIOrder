package pl.karol202.sciorder.client.common.extensions

import pl.karol202.sciorder.common.model.Order

// Status and id could be whatever, will be ignored by the server
fun Order.Companion.create(entries: List<Order.Entry>, details: Order.Details) =
		Order("", "", entries, details, Order.Status.WAITING)

val Order.Status.Companion.DEFAULT_FILTER get() = Order.Status.values().toSet() - Order.Status.DONE - Order.Status.REJECTED
