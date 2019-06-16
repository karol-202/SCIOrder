package pl.karol202.sciorder.common

data class Product(val _id: String,
                   val ownerId: String,
				   val name: String,
				   val available: Boolean,
				   val parameters: List<Parameter>) : Serializable, IdProvider
{
	companion object;

	data class Parameter(val name: String,
	                     val type: Type,
	                     val attributes: Attributes) : Serializable
	{
		enum class Type
		{
			TEXT, INT, FLOAT, BOOL, ENUM
		}

		data class Attributes(val minimalValue: Float? = null,
		                      val maximalValue: Float? = null,
		                      val enumValues: List<String>? = null,
		                      val defaultValue: String? = null) : Serializable
	}

	override val id get() = _id
}
