package pl.karol202.sciorder.common.model

import kotlinx.serialization.Serializable

@Serializable
data class Owner(val _id: String,
                 val name: String,
                 val hash: String) : JvmSerializable, IdProvider
{
	companion object
	{
		private const val MIN_NAME_LENGTH = 3
	}

	override val id get() = _id
	
	val isNameValid get() = name.length >= MIN_NAME_LENGTH
	val isHashValid get() = hash.isNotBlank()
	val isValid get() = isNameValid && isHashValid
}
