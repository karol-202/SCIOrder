package pl.karol202.sciorder.client.common.model.local.order

import androidx.lifecycle.LiveData
import pl.karol202.sciorder.client.common.model.local.CrudDao
import pl.karol202.sciorder.common.model.Order

interface OrderDao : CrudDao<Order>
{
	override fun insert(items: List<Order>)

	override fun update(items: List<Order>)

	fun updateStatus(id: String, status: Order.Status)

	override fun delete(items: List<Order>)

	override fun getAll(): LiveData<List<Order>>
}
