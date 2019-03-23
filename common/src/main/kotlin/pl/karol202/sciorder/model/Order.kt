package pl.karol202.sciorder.model

data class Order(val _id: String,
                 val entries: List<Entry>,
                 val status: Status)
{
	data class Entry(val productId: String,
	                 val quantity: Int,
	                 val parameters: Map<String, String>)

	enum class Status
	{
		WAITING, IN_PROGRESS, DELIVERING, DONE, REJECTED
	}
}
