package pl.karol202.sciorder.common.validation

import pl.karol202.sciorder.common.model.Product
import pl.karol202.sciorder.common.util.isValidFloat
import pl.karol202.sciorder.common.util.isValidInt

// PRODUCT

val Product.Companion.MAX_NAME_LENGTH get() = 30

val Product.isValid get() = isNameValid && areParametersValid && areParametersDistinct
val Product.isNameValid get() = isNameNotBlank && isNameShortEnough
val Product.isNameNotBlank get() = name.isNotBlank()
val Product.isNameShortEnough get() = name.length <= Product.MAX_NAME_LENGTH
val Product.areParametersValid get() = parameters.all { it.isValid }
val Product.areParametersDistinct get() = duplicatedParameterNames.isEmpty()
val Product.duplicatedParameterNames get() =
	parameters.groupBy { it.name }.mapValues { (_, params) -> params.count() }.filterValues { it > 1 }.keys

// PARAMETER

val Product.Parameter.Companion.MAX_NAME_LENGTH get() = 30
val Product.Parameter.Companion.MAX_VALUE_LENGTH get() = 30

val Product.Parameter.isValid get() = isNameValid && areAttributesValid
val Product.Parameter.isNameValid get() = isNameNotBlank && isNameShortEnough
val Product.Parameter.isNameNotBlank get() = name.isNotBlank()
val Product.Parameter.isNameShortEnough get() = name.length <= Product.Parameter.MAX_NAME_LENGTH
val Product.Parameter.areAttributesValid get() = attributes.areValidFor(type)

// ATTRIBUTES

private val Product.Parameter.Attributes.realMinimalValue get() = minimalValue ?: Float.NEGATIVE_INFINITY
private val Product.Parameter.Attributes.realMaximalValue get() = maximalValue ?: Float.POSITIVE_INFINITY
private val Product.Parameter.Attributes.range get() = realMinimalValue..realMaximalValue

val Product.Parameter.Attributes.areEnumValuesShortEnough
	get() = enumValues.orEmpty().all { it.length <= Product.Parameter.MAX_VALUE_LENGTH }
val Product.Parameter.Attributes.isDefaultValueShortEnough
	get() = (defaultValue?.length ?: 0) <= Product.Parameter.MAX_VALUE_LENGTH

fun Product.Parameter.Attributes.areValidFor(type: Product.Parameter.Type) =
		areEnumValuesShortEnough &&
		isDefaultValueShortEnough &&
		isMinimalValueValidFor(type) &&
		isMaximalValueValidFor(type) &&
		areEnumValuesValidFor(type) &&
		isDefaultValueValidFor(type)

fun Product.Parameter.Attributes.isMinimalValueValidFor(type: Product.Parameter.Type) = when(type)
{
	Product.Parameter.Type.INT -> minimalValue == null || (minimalValue.isValidInt() && minimalValue < realMaximalValue)
	Product.Parameter.Type.FLOAT -> minimalValue == null || minimalValue < realMaximalValue
	else -> true
}

fun Product.Parameter.Attributes.isMaximalValueValidFor(type: Product.Parameter.Type) = when(type)
{
	Product.Parameter.Type.INT -> maximalValue == null || (maximalValue.isValidInt() && maximalValue > realMinimalValue)
	Product.Parameter.Type.FLOAT -> maximalValue == null || maximalValue > realMinimalValue
	else -> true
}

fun Product.Parameter.Attributes.areEnumValuesValidFor(type: Product.Parameter.Type) = when(type)
{
	Product.Parameter.Type.ENUM -> enumValues?.isNotEmpty() ?: false
	else -> true
}

fun Product.Parameter.Attributes.isDefaultValueValidFor(type: Product.Parameter.Type) = when(type)
{
	Product.Parameter.Type.INT -> defaultValue == null || (defaultValue.isValidInt() && defaultValue.toFloat() in range)
	Product.Parameter.Type.FLOAT -> defaultValue == null || (defaultValue.isValidFloat() && defaultValue.toFloat() in range)
	Product.Parameter.Type.ENUM -> defaultValue == null || defaultValue in enumValues ?: emptyList()
	else -> true
}
