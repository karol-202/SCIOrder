package pl.karol202.sciorder.model

data class Product(val _id: String,
				   val name: String,
				   val available: Boolean,
				   val parameters: List<Parameter>)
{
	data class Parameter(val name: String,
	                     val type: Type,
	                     val attributes: Attributes)
	{
		enum class Type
		{
			TEXT, DECIMAL, FLOAT, BOOLEAN, ENUM
		}

		data class Attributes(val minimalValue: Float?,
		                      val maximalValue: Float?,
		                      val enumValues: List<String>?)
	}
}
