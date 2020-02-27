package pl.karol202.sciorder.common.validation

import pl.karol202.sciorder.common.model.Admin
import pl.karol202.sciorder.common.request.AdminRequest

val Admin.Companion.MIN_NAME_LENGTH get() = 3
val Admin.Companion.MAX_NAME_LENGTH get() = 30
val Admin.Companion.MIN_PASSWORD_LENGTH get() = 6
val Admin.Companion.MAX_PASSWORD_LENGTH get() = 64

val AdminRequest.isValid get() = isNameLongEnough && isNameShortEnough && isPasswordLongEnough && isPasswordShortEnough
val AdminRequest.isNameLongEnough get() = name.length >= Admin.MIN_NAME_LENGTH
val AdminRequest.isNameShortEnough get() = name.length <= Admin.MAX_NAME_LENGTH
val AdminRequest.isPasswordLongEnough get() = password.length >= Admin.MIN_PASSWORD_LENGTH
val AdminRequest.isPasswordShortEnough get() = password.length <= Admin.MAX_PASSWORD_LENGTH
