package pl.karol202.sciorder.model

import java.io.Serializable

data class Product(val _id: String,
				   val name: String,
				   val available: Boolean,
				   val parameters: List<Parameter>) : Serializable
{
	data class Parameter(val name: String,
	                     val type: Type,
	                     val attributes: Attributes)
	{
		enum class Type
		{
			TEXT, INT, FLOAT, BOOL, ENUM
		}

		data class Attributes(val minimalValue: Float? = null,
		                      val maximalValue: Float? = null,
		                      val enumValues: List<String>? = null,
		                      val defaultValue: String? = null)
	}
}
