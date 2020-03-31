package pl.karol202.sciorder.common.model

import kotlinx.serialization.Serializable
import pl.karol202.sciorder.common.util.IdProvider
import pl.karol202.sciorder.common.util.JvmSerializable

@Serializable
data class Order(override val id: Long,
                 val storeId: Long,
                 val entries: List<OrderEntry>,
                 val details: Details,
                 val status: Status) : JvmSerializable, IdProvider<Long>
{
	@Serializable
	data class Details(val location: String,
	                   val recipient: String) : JvmSerializable

	@Serializable
	enum class Status(val isActive: Boolean)
	{
		WAITING(true),
		IN_PROGRESS(true),
		DELIVERING(true),
		DONE(false),
		REJECTED(false);

		companion object
		{
			fun getByName(name: String) = values().find { it.name == name }
		}
	}
	
	val isActive get() = status.isActive
}
