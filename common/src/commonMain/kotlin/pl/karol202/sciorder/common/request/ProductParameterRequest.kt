package pl.karol202.sciorder.common.request

import kotlinx.serialization.Serializable
import pl.karol202.sciorder.common.model.ProductParameter
import pl.karol202.sciorder.common.util.JvmSerializable

@Serializable
data class ProductParameterRequest(val name: String,
                                   val type: ProductParameter.Type,
                                   val attributes: ProductParameter.Attributes) : JvmSerializable
