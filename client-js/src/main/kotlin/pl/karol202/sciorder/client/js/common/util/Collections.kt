package pl.karol202.sciorder.client.js.common.util

fun <T> Iterable<T>.replace(old: T, new: T) = map { if(it == old) new else it }
