package pl.karol202.sciorder.client.js.common.view

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import pl.karol202.sciorder.client.common.util.Event
import pl.karol202.sciorder.client.common.util.observe
import pl.karol202.sciorder.client.common.util.observeEvent
import react.RComponent
import react.RProps
import react.RState
import react.setState

abstract class View<P : RProps, S : RState> : RComponent<P, S>
{
	private val job = Job()
	private val coroutineScope = CoroutineScope(job)

	constructor() : super()

	constructor(props: P) : super(props)

	final override fun S.init(props: P) = init()

	override fun componentWillUnmount()
	{
		job.cancel()
	}

	fun <T> Flow<T>.bindToState(buildState: S.(T) -> Unit) = observe(coroutineScope) {
		setState { buildState(it) }
	}

	fun <T> Flow<Event<T>>.bindEventToState(buildState: S.(T) -> Unit) = observeEvent(coroutineScope) {
		setState { buildState(it) }
	}
}
