package pl.karol202.sciorder.client.common.extensions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.broadcastIn

fun <T> Flow<T>.shareIn(scope: CoroutineScope) = broadcastIn(scope).asFlow()