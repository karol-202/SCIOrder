package pl.karol202.sciorder.common.validation

import pl.karol202.sciorder.common.model.Admin

val Admin.Companion.MIN_NAME_LENGTH get() = 3
val Admin.Companion.MAX_NAME_LENGTH get() = 30

val Admin.isValid get() = isNameLongEnough && isNameShortEnough
val Admin.isNameLongEnough get() = name.length >= Admin.MIN_NAME_LENGTH
val Admin.isNameShortEnough get() = name.length <= Admin.MAX_NAME_LENGTH
