package pl.karol202.sciorder.common.validation

import pl.karol202.sciorder.common.model.ProductParameter
import pl.karol202.sciorder.common.request.ProductParameterRequest
import pl.karol202.sciorder.common.util.isValidFloat
import pl.karol202.sciorder.common.util.isValidInt

val ProductParameter.Companion.MAX_NAME_LENGTH get() = 30
val ProductParameter.Companion.MAX_VALUE_LENGTH get() = 30

val ProductParameterRequest.isValid get() = isNameValid && areAttributesValid
val ProductParameterRequest.isNameValid get() = isNameNotBlank && isNameShortEnough
val ProductParameterRequest.isNameNotBlank get() = name.isNotBlank()
val ProductParameterRequest.isNameShortEnough get() = name.length <= ProductParameter.MAX_NAME_LENGTH
val ProductParameterRequest.areAttributesValid get() = attributes.areValidFor(type)

// ATTRIBUTES

private val ProductParameter.Attributes.realMinimalValue get() = minimalValue ?: Float.NEGATIVE_INFINITY
private val ProductParameter.Attributes.realMaximalValue get() = maximalValue ?: Float.POSITIVE_INFINITY
private val ProductParameter.Attributes.range get() = realMinimalValue..realMaximalValue

val ProductParameter.Attributes.areEnumValuesShortEnough
	get() = enumValues.orEmpty().all { it.length <= ProductParameter.MAX_VALUE_LENGTH }
val ProductParameter.Attributes.isDefaultValueShortEnough
	get() = (defaultValue?.length ?: 0) <= ProductParameter.MAX_VALUE_LENGTH

fun ProductParameter.Attributes.areValidFor(type: ProductParameter.Type) =
		areEnumValuesShortEnough &&
				isDefaultValueShortEnough &&
				isMinimalValueValidFor(type) &&
				isMaximalValueValidFor(type) &&
				areEnumValuesValidFor(type) &&
				isDefaultValueValidFor(type)

fun ProductParameter.Attributes.isMinimalValueValidFor(type: ProductParameter.Type) = when(type)
{
	ProductParameter.Type.INT -> minimalValue == null || (minimalValue.isValidInt() && minimalValue < realMaximalValue)
	ProductParameter.Type.FLOAT -> minimalValue == null || minimalValue < realMaximalValue
	else -> minimalValue == null
}

fun ProductParameter.Attributes.isMaximalValueValidFor(type: ProductParameter.Type) = when(type)
{
	ProductParameter.Type.INT -> maximalValue == null || (maximalValue.isValidInt() && maximalValue > realMinimalValue)
	ProductParameter.Type.FLOAT -> maximalValue == null || maximalValue > realMinimalValue
	else -> maximalValue == null
}

fun ProductParameter.Attributes.areEnumValuesValidFor(type: ProductParameter.Type) = when(type)
{
	ProductParameter.Type.ENUM -> enumValues?.isNotEmpty() ?: false
	else -> enumValues?.isEmpty() ?: true
}

fun ProductParameter.Attributes.isDefaultValueValidFor(type: ProductParameter.Type) = when(type)
{
	ProductParameter.Type.INT -> defaultValue == null || (defaultValue.isValidInt() && defaultValue.toFloat() in range)
	ProductParameter.Type.FLOAT -> defaultValue == null || (defaultValue.isValidFloat() && defaultValue.toFloat() in range)
	ProductParameter.Type.ENUM -> defaultValue == null || defaultValue in enumValues.orEmpty()
	else -> true
}
