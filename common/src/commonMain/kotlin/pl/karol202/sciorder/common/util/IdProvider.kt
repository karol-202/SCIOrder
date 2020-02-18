package pl.karol202.sciorder.common.util

interface IdProvider
{
	val id: Any
}

val Iterable<IdProvider>.ids get() = map { it.id }
