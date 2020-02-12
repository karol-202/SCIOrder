package pl.karol202.sciorder.server.util

interface MappableEntity<M : Any>
{
	fun toModel(): M
}

interface SortableEntity
{
	val ordinal: Int
}

inline fun <reified M : Any, E : MappableEntity<M>> Iterable<E>.toModels() =
		map { it.toModel() }

@JvmName("toSortedModels")
inline fun <reified M : Any, E> Iterable<E>.toModels() where E : MappableEntity<M>, E : SortableEntity =
		sortedBy { it.ordinal }.map { it.toModel() }
