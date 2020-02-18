package pl.karol202.sciorder.common.request

import kotlinx.serialization.Serializable
import pl.karol202.sciorder.common.util.JvmSerializable

@Serializable
data class AdminRequest(val name: String): JvmSerializable
