package pl.karol202.sciorder.common.validation

import pl.karol202.sciorder.common.model.Admin
import pl.karol202.sciorder.common.request.AdminRequest

val Admin.Companion.MIN_NAME_LENGTH get() = 3
val Admin.Companion.MAX_NAME_LENGTH get() = 30

val AdminRequest.isValid get() = isNameLongEnough && isNameShortEnough
val AdminRequest.isNameLongEnough get() = name.length >= Admin.MIN_NAME_LENGTH
val AdminRequest.isNameShortEnough get() = name.length <= Admin.MAX_NAME_LENGTH
