package pl.karol202.sciorder.client.js.common.model.local

import pl.karol202.sciorder.common.IdProvider

interface FakeDao
{
	class IdUniqueElement<T : IdProvider>(val value: T)
	{
		val id get() = value.id

		override fun equals(other: Any?) = value.id == (other as? IdProvider)

		override fun hashCode() = value.id.hashCode()
	}

	fun <T : IdProvider> T.wrap() = IdUniqueElement(this)

	fun <T : IdProvider> Collection<T>.wrap() = map { it.wrap() }

	fun <T : IdProvider> Collection<IdUniqueElement<T>>.values() = map { it.value }

	fun <T : IdProvider> Set<IdUniqueElement<T>>.update(element: IdUniqueElement<T>) = map { if(it.id == element.id) element else it }.toSet()

	tailrec fun <T : IdProvider> Set<IdUniqueElement<T>>.update(element: List<IdUniqueElement<T>>): Set<IdUniqueElement<T>> =
			if(element.isEmpty()) this
			else
			{
				update(element.first())
				update(element.drop(1))
			}
}
