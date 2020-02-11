package pl.karol202.sciorder.common.model

import kotlinx.serialization.Serializable

@Serializable
data class Store(override val id: Int,
                 val name: String) : JvmSerializable, IdProvider
