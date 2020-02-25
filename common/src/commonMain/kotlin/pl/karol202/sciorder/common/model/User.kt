package pl.karol202.sciorder.common.model

import kotlinx.serialization.Serializable
import pl.karol202.sciorder.common.util.IdProvider
import pl.karol202.sciorder.common.util.JvmSerializable

@Serializable
data class User(override val id: Long) : JvmSerializable, IdProvider
