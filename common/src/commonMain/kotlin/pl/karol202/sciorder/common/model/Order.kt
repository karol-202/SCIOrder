package pl.karol202.sciorder.common.model

import kotlinx.serialization.Serializable

@Serializable
data class Order(override val id: Int,
                 val storeId: Int,
                 val entries: List<Entry>,
                 val details: Details,
                 val status: Status) : JvmSerializable, IdProvider
{
	companion object;

	@Serializable
	data class Entry(val productId: Int,
	                 val quantity: Int,
	                 val parameters: Map<Int, String>) : JvmSerializable

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
