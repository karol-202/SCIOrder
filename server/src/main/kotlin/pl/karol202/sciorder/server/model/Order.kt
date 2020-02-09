package pl.karol202.sciorder.server.model

import pl.karol202.sciorder.common.model.Order
import pl.karol202.sciorder.common.model.Product
import pl.karol202.sciorder.common.util.isValidFloat
import pl.karol202.sciorder.common.util.isValidInt
import pl.karol202.sciorder.server.database.ProductDao
import pl.karol202.sciorder.server.util.equalsIgnoreOrder

suspend fun Order.isValid(productDao: ProductDao) = entries.all { it.isValid(productDao, ownerId) }

private suspend fun Order.Entry.isValid(productDao: ProductDao, ownerId: String) =
		productDao.getProductById(ownerId, productId)?.let { isValid(it) } ?: false

private fun Order.Entry.isValid(product: Product) = product.available && quantity > 0 && isParamsListValid(product)

private fun Order.Entry.isParamsListValid(product: Product) =
		product.parameters.map { it.name }.equalsIgnoreOrder(parameters.keys) && parameters.all { (name, value) -> product.isParamValid(name, value) }

private fun Product.isParamValid(name: String, value: String): Boolean
{
	fun Product.Parameter.valueRange(): ClosedFloatingPointRange<Float>
	{
		val minValue = attributes.minimalValue ?: Float.MIN_VALUE
		val maxValue = attributes.maximalValue ?: Float.MAX_VALUE
		return minValue..maxValue
	}

	val productParameter = parameters.singleOrNull { it.name == name }
	return when(productParameter?.type)
	{
		null -> false
		Product.Parameter.Type.INT -> value.isValidInt() && value.toFloat() in productParameter.valueRange()
		Product.Parameter.Type.FLOAT -> value.isValidFloat() && value.toFloat() in productParameter.valueRange()
		Product.Parameter.Type.ENUM -> value in (productParameter.attributes.enumValues ?: emptyList())
		else -> true
	}
}
