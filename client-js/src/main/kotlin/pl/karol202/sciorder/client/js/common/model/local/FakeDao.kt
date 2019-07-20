package pl.karol202.sciorder.client.js.common.model.local

import kotlinx.serialization.Serializable
import pl.karol202.sciorder.common.IdProvider

interface FakeDao
{
	@Serializable
	class IdUniqueElement<T : IdProvider>(val value: T)
	{
		val id get() = value.id

		override fun equals(other: Any?) = id == (other as? IdUniqueElement<*>)?.id

		override fun hashCode() = value.id.hashCode()
	}

	fun <T : IdProvider> T.wrap() = IdUniqueElement(this)

	fun <T : IdProvider> Collection<T>.wrap() = map { it.wrap() }

	fun <T : IdProvider> Collection<IdUniqueElement<T>>.values() = map { it.value }

	fun <T : IdProvider> Set<IdUniqueElement<T>>.update(element: IdUniqueElement<T>) =
			map { if(it.id == element.id) element else it }.toSet()

	tailrec fun <T : IdProvider> Set<IdUniqueElement<T>>.update(elements: List<IdUniqueElement<T>>): Set<IdUniqueElement<T>> =
			if(elements.isEmpty()) this
			else update(elements.first()).update(elements.drop(1))
}
