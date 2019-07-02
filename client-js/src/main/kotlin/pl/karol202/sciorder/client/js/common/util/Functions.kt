package pl.karol202.sciorder.client.js.common.util

fun <T1> Iterable<(T1) -> Unit>.invokeEach(arg1: T1) = forEach { it(arg1) }
