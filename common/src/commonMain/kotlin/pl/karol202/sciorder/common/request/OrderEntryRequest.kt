package pl.karol202.sciorder.common.request

import kotlinx.serialization.Serializable
import pl.karol202.sciorder.common.util.JvmSerializable

@Serializable
data class OrderEntryRequest(val productId: Long,
                             val quantity: Int,
                             val parameters: Map<Long, String>) : JvmSerializable
