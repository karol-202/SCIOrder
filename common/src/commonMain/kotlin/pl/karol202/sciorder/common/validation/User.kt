package pl.karol202.sciorder.common.validation

import pl.karol202.sciorder.common.model.User
import pl.karol202.sciorder.common.request.UserRequest

val User.Companion.PASSWORD_LENGTH get() = 32

val UserRequest.isValid get() = isPasswordLengthValid
val UserRequest.isPasswordLengthValid get() = password.length == User.PASSWORD_LENGTH
