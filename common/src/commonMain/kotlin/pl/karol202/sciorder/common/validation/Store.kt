package pl.karol202.sciorder.common.validation

import pl.karol202.sciorder.common.model.Store
import pl.karol202.sciorder.common.request.StoreRequest

val Store.Companion.MIN_NAME_LENGTH get() = 3
val Store.Companion.MAX_NAME_LENGTH get() = 30

val StoreRequest.isValid get() = isNameLongEnough && isNameShortEnough
val StoreRequest.isNameLongEnough get() = name.length >= Store.MIN_NAME_LENGTH
val StoreRequest.isNameShortEnough get() = name.length <= Store.MAX_NAME_LENGTH
