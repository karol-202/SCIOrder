package pl.karol202.sciorder.common.util

interface IdProvider<I : Any>
{
	val id: I
}

fun <I : Any> Iterable<IdProvider<I>>.ids() = map { it.id }
