package pl.karol202.sciorder.common.validation

import pl.karol202.sciorder.common.model.Product
import pl.karol202.sciorder.common.request.ProductCreateRequest
import pl.karol202.sciorder.common.request.ProductRequest
import pl.karol202.sciorder.common.request.ProductUpdateRequest

val Product.Companion.MAX_NAME_LENGTH get() = 30

val ProductRequest.isNameValid get() = isNameNotBlank && isNameShortEnough
val ProductRequest.isNameNotBlank get() = name.isNotBlank()
val ProductRequest.isNameShortEnough get() = name.length <= Product.MAX_NAME_LENGTH

val ProductCreateRequest.isValid get() = isNameValid && areParametersValid
val ProductCreateRequest.areParametersValid get() = createdParameters.all { it.isValid }

val ProductUpdateRequest.isValid get() = isNameValid && areParametersValid
val ProductUpdateRequest.areParametersValid get() = (createdParameters + updatedParameters.values).all { it.isValid }
