package pl.karol202.sciorder.common.request

import kotlinx.serialization.Serializable
import pl.karol202.sciorder.common.util.JvmSerializable

@Serializable
data class ProductRequest(val name: String,
                          val available: Boolean) : JvmSerializable
