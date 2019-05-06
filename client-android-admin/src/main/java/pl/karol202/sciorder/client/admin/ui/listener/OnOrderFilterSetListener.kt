package pl.karol202.sciorder.client.admin.ui.listener

import pl.karol202.sciorder.common.model.Order

interface OnOrderFilterSetListener
{
	fun onOrderFilterSet(filter: Set<Order.Status>)
}
