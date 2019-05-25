package pl.karol202.sciorder.common.model

import java.io.Serializable

data class Owner(val _id: String,
                 val name: String,
                 val hash: String) : Serializable, IdProvider
{
	companion object;

	override val id get() = _id
}
