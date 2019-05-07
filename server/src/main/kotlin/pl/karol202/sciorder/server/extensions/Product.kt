package pl.karol202.sciorder.server.extensions

import pl.karol202.sciorder.common.model.Product
import pl.karol202.sciorder.server.util.hasDuplicates

fun Product.isValid() = parameters.all { it.isValid() } && !parameters.hasDuplicates { it.name }

private fun Product.Parameter.isValid() = with(attributes) {
	when(type)
	{
		Product.Parameter.Type.INT -> defaultValue?.let { it.toIntOrNull() != null } ?: true
		Product.Parameter.Type.FLOAT -> defaultValue?.let { it.toFloatOrNull() != null } ?: true
		Product.Parameter.Type.ENUM -> !enumValues.isNullOrEmpty() && defaultValue?.let { it in enumValues!! } ?: true
		else -> true
	}
}
