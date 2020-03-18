package pl.karol202.sciorder.server.util

interface Mappable<M : Any>
{
	fun map(): M
}

fun <M : Any, E : Mappable<M>> Iterable<E>.map() = map { it.map() }
