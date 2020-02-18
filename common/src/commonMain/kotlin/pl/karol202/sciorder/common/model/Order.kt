package pl.karol202.sciorder.common.model

import kotlinx.serialization.Serializable
import pl.karol202.sciorder.common.util.IdProvider
import pl.karol202.sciorder.common.util.JvmSerializable

@Serializable
data class Order(override val id: Long,
                 val storeId: Long,
                 val entries: List<OrderEntry>,
                 val details: Details,
                 val status: Status) : JvmSerializable, IdProvider
{
	@Serializable
	data class Details(val location: String,
	                   val recipient: String) : JvmSerializable

	enum class Status
	{
		WAITING, IN_PROGRESS, DELIVERING, DONE, REJECTED;

		companion object
		{
			fun getByName(name: String) = values().find { it.name == name }
		}
	}
}
