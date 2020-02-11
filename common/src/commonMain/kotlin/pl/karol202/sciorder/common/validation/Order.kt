package pl.karol202.sciorder.common.validation

import pl.karol202.sciorder.common.model.Order
import pl.karol202.sciorder.common.model.Product

// ENTRY

val Order.Entry.areParametersValuesShortEnough
	get() = parameters.values.all { it.length <= Product.Parameter.MAX_VALUE_LENGTH }

// DETAILS

val Order.Details.Companion.MAX_DETAIL_LENGTH get() = 30

val Order.Details.areValid get() = isLocationShortEnough && isRecipientShortEnough
val Order.Details.isLocationShortEnough get() = location.length <= Order.Details.MAX_DETAIL_LENGTH
val Order.Details.isRecipientShortEnough get() = recipient.length <= Order.Details.MAX_DETAIL_LENGTH
