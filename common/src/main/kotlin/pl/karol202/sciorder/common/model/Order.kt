package pl.karol202.sciorder.common.model

import java.io.Serializable

data class Order(val _id: String,
                 val entries: List<Entry>,
                 val details: Details,
                 val status: Status) : Serializable
{
	companion object;

	data class Entry(val productId: String,
	                 val quantity: Int,
	                 val parameters: Map<String, String>) : Serializable

	data class Details(val location: String,
	                   val recipient: String) : Serializable

	enum class Status
	{
		WAITING, IN_PROGRESS, DELIVERING, DONE, REJECTED
	}
}
