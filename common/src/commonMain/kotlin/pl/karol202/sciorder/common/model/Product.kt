package pl.karol202.sciorder.common.model

import kotlinx.serialization.Serializable
import pl.karol202.sciorder.common.util.IdProvider
import pl.karol202.sciorder.common.util.JvmSerializable

@Serializable
data class Product(override val id: Long,
                   val storeId: Long,
				   val name: String,
				   val available: Boolean,
				   val parameters: List<ProductParameter>) : JvmSerializable, IdProvider
