package pl.karol202.sciorder.server.entity.mapping

interface Mappable<M : Any>
{
	fun map(): M
}

interface OrderedMappable<M : Any> : Mappable<M>
{
	val ordinal: Int
}

fun <M : Any, E : Mappable<M>> Iterable<E>.map() =
		map { it.map() }

@JvmName("toSortedModels")
fun <M : Any, E : OrderedMappable<M>> Iterable<E>.map() =
		sortedBy { it.ordinal }.map { it.map() }
