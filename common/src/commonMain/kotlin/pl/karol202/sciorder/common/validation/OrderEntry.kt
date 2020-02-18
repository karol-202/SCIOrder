package pl.karol202.sciorder.common.validation

import pl.karol202.sciorder.common.model.ProductParameter
import pl.karol202.sciorder.common.request.OrderEntryRequest

val OrderEntryRequest.areParametersValuesShortEnough
	get() = parameters.values.all { it.length <= ProductParameter.MAX_VALUE_LENGTH }
