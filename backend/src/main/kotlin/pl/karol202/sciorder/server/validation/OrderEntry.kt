package pl.karol202.sciorder.server.validation

import pl.karol202.sciorder.common.model.Product
import pl.karol202.sciorder.common.model.ProductParameter
import pl.karol202.sciorder.common.request.OrderEntryRequest
import pl.karol202.sciorder.common.util.isValidFloat
import pl.karol202.sciorder.common.util.isValidInt
import pl.karol202.sciorder.common.validation.areParametersValuesShortEnough
import pl.karol202.sciorder.server.util.equalsIgnoreOrder

fun OrderEntryRequest.isValid(product: Product) =
		areParametersValuesShortEnough &&
		product.available &&
		quantity > 0 &&
		isParamsListValid(product)

private fun OrderEntryRequest.isParamsListValid(product: Product) =
		product.parameters.map { it.id }.equalsIgnoreOrder(parameters.keys) &&
		parameters.all { (paramId, value) -> product.isParamValid(paramId, value) }

private fun Product.isParamValid(paramId: Long, value: String): Boolean
{
	fun ProductParameter.valueRange(): ClosedFloatingPointRange<Float>
	{
		val minValue = attributes.minimalValue ?: Float.MIN_VALUE
		val maxValue = attributes.maximalValue ?: Float.MAX_VALUE
		return minValue..maxValue
	}

	val productParameter = parameters.singleOrNull { it.id == paramId } ?: return false
	return when(productParameter.type)
	{
		ProductParameter.Type.INT -> value.isValidInt() && value.toFloat() in productParameter.valueRange()
		ProductParameter.Type.FLOAT -> value.isValidFloat() && value.toFloat() in productParameter.valueRange()
		ProductParameter.Type.ENUM -> value in productParameter.attributes.enumValues.orEmpty()
		else -> true
	}
}
