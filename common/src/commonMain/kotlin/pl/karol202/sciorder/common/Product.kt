package pl.karol202.sciorder.common

import kotlinx.serialization.Serializable

@Serializable
data class Product(val _id: String,
                   val ownerId: String,
				   val name: String,
				   val available: Boolean,
				   val parameters: List<Parameter>) : JvmSerializable, IdProvider
{
	companion object;

	@Serializable
	data class Parameter(val name: String,
	                     val type: Type,
	                     val attributes: Attributes) : JvmSerializable
	{
		enum class Type
		{
			TEXT, INT, FLOAT, BOOL, ENUM
		}

		@Serializable
		data class Attributes(val minimalValue: Float? = null,
		                      val maximalValue: Float? = null,
		                      val enumValues: List<String>? = null,
		                      val defaultValue: String? = null) : JvmSerializable
	}

	override val id get() = _id
}
