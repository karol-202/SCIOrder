package pl.karol202.sciorder.common.model

import kotlinx.serialization.Serializable
import pl.karol202.sciorder.common.util.isValidFloat
import pl.karol202.sciorder.common.util.isValidInt

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
		{
			private val realMinimalValue get() = minimalValue ?: Float.NEGATIVE_INFINITY
			private val realMaximalValue get() = maximalValue ?: Float.POSITIVE_INFINITY
			private val range get() = realMinimalValue..realMaximalValue
			
			fun areValidFor(type: Type) = isMinimalValueValidFor(type) &&
										  isMaximalValueValidFor(type) &&
										  areEnumValuesValidFor(type) &&
										  isDefaultValueValidFor(type)
			
			fun isMinimalValueValidFor(type: Type) = when(type)
			{
				Type.INT -> minimalValue == null || (minimalValue.isValidInt() && minimalValue < realMaximalValue)
				Type.FLOAT -> minimalValue == null || minimalValue < realMaximalValue
				else -> true
			}
			
			fun isMaximalValueValidFor(type: Type) = when(type)
			{
				Type.INT -> maximalValue == null || (maximalValue.isValidInt() && maximalValue > realMinimalValue)
				Type.FLOAT -> maximalValue == null || maximalValue > realMinimalValue
				else -> true
			}
			
			fun areEnumValuesValidFor(type: Type) = when(type)
			{
				Type.ENUM -> enumValues?.isNotEmpty() ?: false
				else -> true
			}
			
			fun isDefaultValueValidFor(type: Type) = when(type)
			{
				Type.INT -> defaultValue == null || (defaultValue.isValidInt() && defaultValue.toFloat() in range)
				Type.FLOAT -> defaultValue == null || (defaultValue.isValidFloat() && defaultValue.toFloat() in range)
				Type.ENUM -> defaultValue == null || defaultValue in enumValues ?: emptyList()
				else -> true
			}
		}
		
		val isNameValid get() = name.isNotBlank()
		val areAttributesValid get() = attributes.areValidFor(type)
		val isValid get() = isNameValid && areAttributesValid
	}

	override val id get() = _id
	
	val isNameValid get() = name.isNotBlank()
	val areParametersValid get() = parameters.all { it.isValid }
	val areParametersDistinct get() = parameters.duplicatedParameterNames.isEmpty()
	val isValid get() = isNameValid && areParametersValid && areParametersDistinct
}

val List<Product.Parameter>.duplicatedParameterNames get() =
	this.groupBy { it.name }.mapValues { (_, params) -> params.count() }.filterValues { it > 1 }.keys
