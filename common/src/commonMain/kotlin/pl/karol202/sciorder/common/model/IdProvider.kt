package pl.karol202.sciorder.common.model

interface IdProvider
{
	val id: Any
}

val Iterable<IdProvider>.ids get() = map { it.id }
