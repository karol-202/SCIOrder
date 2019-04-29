package pl.karol202.sciorder.extensions

import pl.karol202.sciorder.model.Order

// Status could be whatever, will be ignored by the server
fun Order.Companion.create(entries: List<Order.Entry>) = Order("", entries, Order.Status.WAITING)
