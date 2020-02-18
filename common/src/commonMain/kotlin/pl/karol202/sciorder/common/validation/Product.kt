package pl.karol202.sciorder.common.validation

import pl.karol202.sciorder.common.model.Product
import pl.karol202.sciorder.common.request.ProductRequest

val Product.Companion.MAX_NAME_LENGTH get() = 30

val ProductRequest.isValid get() = isNameValid
val ProductRequest.isNameValid get() = isNameNotBlank && isNameShortEnough
val ProductRequest.isNameNotBlank get() = name.isNotBlank()
val ProductRequest.isNameShortEnough get() = name.length <= Product.MAX_NAME_LENGTH
