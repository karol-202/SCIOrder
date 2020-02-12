package pl.karol202.sciorder.common.model

import kotlinx.serialization.Serializable

@Serializable
data class Product(override val id: Long,
                   val storeId: Long,
				   val name: String,
				   val available: Boolean,
				   val parameters: List<Parameter>) : JvmSerializable, IdProvider
{
	companion object;

	@Serializable
	data class Parameter(val id: Long,
	                     val name: String,
	                     val type: Type,
	                     val attributes: Attributes) : JvmSerializable
	{
		enum class Type
		{
			TEXT, INT, FLOAT, BOOL, ENUM;
			
			companion object
			{
				fun getByName(name: String) = values().find { it.name == name }
			}
		}

		@Serializable
		data class Attributes(val minimalValue: Float? = null,
		                      val maximalValue: Float? = null,
		                      val enumValues: List<String>? = null,
		                      val defaultValue: String? = null) : JvmSerializable
	}
}
