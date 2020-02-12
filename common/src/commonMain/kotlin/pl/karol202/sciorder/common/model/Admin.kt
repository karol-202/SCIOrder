package pl.karol202.sciorder.common.model

import kotlinx.serialization.Serializable

@Serializable
data class Admin(override val id: Long,
                 val name: String) : JvmSerializable, IdProvider
