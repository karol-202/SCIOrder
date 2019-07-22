package pl.karol202.sciorder.client.js.common.util

fun <T> Iterable<T>.replace(old: T, new: T) = map { if(it == old) new else it }

fun <T> Iterable<T>.replaceIndex(index: Int, new: T) = mapIndexed { i, v -> if(i == index) new else v }

fun <T : Any> Iterable<T>.removeIndex(index: Int) = mapIndexedNotNull { i, v -> if(i == index) null else v }
