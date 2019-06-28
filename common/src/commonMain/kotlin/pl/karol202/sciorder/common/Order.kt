package pl.karol202.sciorder.common

import kotlinx.serialization.Serializable

@Serializable
data class Order(val _id: String,
                 val ownerId: String,
                 val entries: List<Entry>,
                 val details: Details,
                 val status: Status) : JvmSerializable, IdProvider
{
	companion object;

	@Serializable
	data class Entry(val productId: String,
	                 val quantity: Int,
	                 val parameters: Map<String, String>) : JvmSerializable

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

	override val id get() = _id
}
