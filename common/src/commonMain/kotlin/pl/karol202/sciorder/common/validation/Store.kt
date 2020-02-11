package pl.karol202.sciorder.common.validation

import pl.karol202.sciorder.common.model.Store

val Store.Companion.MIN_NAME_LENGTH get() = 3
val Store.Companion.MAX_NAME_LENGTH get() = 30

val Store.isValid get() = isNameLongEnough && isNameShortEnough
val Store.isNameLongEnough get() = name.length >= Store.MIN_NAME_LENGTH
val Store.isNameShortEnough get() = name.length <= Store.MAX_NAME_LENGTH
