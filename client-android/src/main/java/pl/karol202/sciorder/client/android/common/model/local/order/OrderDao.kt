package pl.karol202.sciorder.client.android.common.model.local.order

import androidx.lifecycle.LiveData
import pl.karol202.sciorder.client.android.common.model.local.CrudDao
import pl.karol202.sciorder.common.model.Order

interface OrderDao : CrudDao<Order>
{
	suspend fun updateStatus(id: String, status: Order.Status)

	suspend fun deleteAll()

	fun getByOwnerId(ownerId: String): LiveData<List<Order>>
}
