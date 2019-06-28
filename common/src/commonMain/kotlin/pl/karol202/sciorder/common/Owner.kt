package pl.karol202.sciorder.common

import kotlinx.serialization.Serializable

@Serializable
data class Owner(val _id: String,
                 val name: String,
                 val hash: String) : JvmSerializable, IdProvider
{
	companion object;

	override val id get() = _id
}
