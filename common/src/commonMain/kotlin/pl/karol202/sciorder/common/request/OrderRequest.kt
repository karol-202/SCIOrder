package pl.karol202.sciorder.common.request

import kotlinx.serialization.Serializable
import pl.karol202.sciorder.common.model.Order
import pl.karol202.sciorder.common.util.JvmSerializable

@Serializable
data class OrderRequest(val entries: List<OrderEntryRequest>,
                        val details: Order.Details) : JvmSerializable
