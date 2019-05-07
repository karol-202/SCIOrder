package pl.karol202.sciorder.server.extensions

import pl.karol202.sciorder.common.model.Order
import pl.karol202.sciorder.common.model.Product
import pl.karol202.sciorder.server.dao.ProductDao

suspend fun Order.isValid(productDao: ProductDao) = entries.all { it.isValid(productDao) }

private suspend fun Order.Entry.isValid(productDao: ProductDao): Boolean
{
	val product = productDao.getProductById(productId) ?: return false
	return product.available && quantity > 0 && isParamsListValid(product)
}

private fun Order.Entry.isParamsListValid(product: Product) =
		product.parameters.map { it.name }.equalsIgnoreOrder(parameters.keys) && parameters.all { it.toPair().isParamValid(product) }

private fun <T : Comparable<T>> Collection<T>.equalsIgnoreOrder(other: Collection<T>) = sorted() == other.sorted()

private fun Pair<String, String>.isParamValid(product: Product): Boolean
{
	fun Product.Parameter.valueRange(): ClosedFloatingPointRange<Float>
	{
		val minValue = attributes.minimalValue ?: Float.MIN_VALUE
		val maxValue = attributes.maximalValue ?: Float.MAX_VALUE
		return minValue..maxValue
	}

	val (name, value) = this
	val productParameter = product.parameters.singleOrNull { it.name == name }
	return when(productParameter?.type)
	{
		Product.Parameter.Type.TEXT, Product.Parameter.Type.BOOL -> true
		Product.Parameter.Type.INT -> value.toIntOrNull()?.toFloat()?.let { it in productParameter.valueRange() } ?: false
		Product.Parameter.Type.FLOAT -> value.toFloatOrNull()?.let { it in productParameter.valueRange() } ?: false
		Product.Parameter.Type.ENUM -> value in (productParameter.attributes.enumValues ?: listOf())
		null -> false
	}
}
