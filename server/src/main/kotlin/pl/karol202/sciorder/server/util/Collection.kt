package pl.karol202.sciorder.server.util

fun <T> Collection<T>.hasDuplicates() = distinct().size != size

fun <T, K> Collection<T>.hasDuplicates(selector: (T) -> K) = map(selector).hasDuplicates()
