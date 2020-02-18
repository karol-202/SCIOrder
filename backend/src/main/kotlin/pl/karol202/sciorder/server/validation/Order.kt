package pl.karol202.sciorder.server.validation

import pl.karol202.sciorder.common.model.Product
import pl.karol202.sciorder.common.request.OrderEntryRequest
import pl.karol202.sciorder.common.request.OrderRequest
import pl.karol202.sciorder.common.validation.areValid

suspend fun OrderRequest.isValid(productProvider: suspend (Long) -> Product?) =
		details.areValid && entries.all { it.isValid(productProvider) }

private suspend fun OrderEntryRequest.isValid(productProvider: suspend (Long) -> Product?) =
		productProvider(productId)?.let { isValid(it) } ?: false
