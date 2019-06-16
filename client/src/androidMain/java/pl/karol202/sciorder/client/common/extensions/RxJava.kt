package pl.karol202.sciorder.client.common.extensions

import io.reactivex.Flowable
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.flow.asFlow

private fun <T> T?.wrap() = Wrapper(this)
private data class Wrapper<T>(val unwrap: T?)

fun <T> Flowable<T>.asFlowWrapped(batchSize: Int = 1) = map { it.wrap() }.asFlow(batchSize).map { it.unwrap }
