package pl.karol202.sciorder.server.util

fun <T : Comparable<T>> Collection<T>.equalsIgnoreOrder(other: Collection<T>) = sorted() == other.sorted()
