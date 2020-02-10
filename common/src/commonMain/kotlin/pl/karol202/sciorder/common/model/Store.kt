package pl.karol202.sciorder.common.model

import kotlinx.serialization.Serializable

private const val MIN_NAME_LENGTH = 3

@Serializable
data class Store(override val id: Int,
                 val name: String) : JvmSerializable, IdProvider
{
	val isNameValid get() = name.length >= MIN_NAME_LENGTH
	val isValid get() = isNameValid
}
