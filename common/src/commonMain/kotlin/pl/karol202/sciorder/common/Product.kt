package pl.karol202.sciorder.common

import kotlinx.serialization.Serializable
import pl.karol202.sciorder.common.util.hasDuplicates
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
			private val realMinimalValue = minimalValue ?: Float.MIN_VALUE
			private val realMaximalValue = maximalValue ?: Float.MAX_VALUE
			private val range = realMinimalValue..realMaximalValue
			
			fun areValidFor(type: Type) = isMinimalValueValidFor(type) &&
										 isMaximalValueValidFor(type) &&
										 areEnumValuesValidFor(type) &&
										 isDefaultValueValidFor(type)
			
			fun isMinimalValueValidFor(type: Type) = when(type)
			{
				Type.INT -> minimalValue == null || (minimalValue.toString().isValidInt() && minimalValue < realMaximalValue)
				Type.FLOAT -> minimalValue == null || minimalValue < realMaximalValue
				else -> true
			}
			
			fun isMaximalValueValidFor(type: Type) = when(type)
			{
				Type.INT -> maximalValue == null || (maximalValue.toString().isValidInt() && maximalValue > realMinimalValue)
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
		
		val isNameValid = name.isNotBlank()
		val areAttributesValid = attributes.areValidFor(type)
		val isValid = isNameValid && areAttributesValid
	}

	override val id get() = _id
	
	val isOwnerIdValid = ownerId.isNotBlank()
	val isNameValid = name.isNotBlank()
	val areParametersValid = parameters.all { it.isValid }
	val areParametersDistinct = !parameters.hasDuplicates { it.name }
	val isValid = isOwnerIdValid && isNameValid && areParametersValid && areParametersDistinct
}
