package pl.karol202.sciorder.common.validation

import pl.karol202.sciorder.common.model.User
import pl.karol202.sciorder.common.request.UserRequest

val User.Companion.MIN_PASSWORD_LENGTH get() = 6
val User.Companion.MAX_PASSWORD_LENGTH get() = 64

val UserRequest.isValid get() = isPasswordLongEnough && isPasswordShortEnough
val UserRequest.isPasswordLongEnough get() = password.length >= User.MIN_PASSWORD_LENGTH
val UserRequest.isPasswordShortEnough get() = password.length <= User.MAX_PASSWORD_LENGTH
