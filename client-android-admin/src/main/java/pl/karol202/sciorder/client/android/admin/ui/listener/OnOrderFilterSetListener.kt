package pl.karol202.sciorder.client.android.admin.ui.listener

import pl.karol202.sciorder.common.Order

interface OnOrderFilterSetListener
{
	fun onOrderFilterSet(filter: Set<Order.Status>)
}
