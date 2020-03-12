package pl.karol202.sciorder.common.model

import kotlinx.serialization.Serializable
import pl.karol202.sciorder.common.util.IdProvider
import pl.karol202.sciorder.common.util.JvmSerializable

@Serializable
data class OrderEntry(override val id: Long,
                      val orderId: Long,
                      val productId: Long,
                      val quantity: Int,
                      val parameters: Map<Long, String>) : JvmSerializable, IdProvider
