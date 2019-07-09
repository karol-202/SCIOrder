package pl.karol202.sciorder.client.js.common.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import pl.karol202.sciorder.client.common.util.observe
import react.*
import react.router.dom.RouteResultProps
import react.router.dom.redirect
import react.router.dom.route
import kotlin.reflect.KClass

abstract class ExtendedComponent<P : RProps, S : RState> : RComponent<P, S>
{
	private val job = Job()
	protected val coroutineScope = CoroutineScope(job)

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
}

fun RBuilder.routeElse(render: (RouteResultProps<RProps>) -> ReactElement?) = route("", render = render)

fun RBuilder.routeElse(component: KClass<out RComponent<*, *>>) = route("", component)

fun RBuilder.redirectTo(to: String) = redirect(from = "", to = to)

operator fun <T : RProps> RouteResultProps<T>.component1() = history

operator fun <T : RProps> RouteResultProps<T>.component2() = location

operator fun <T : RProps> RouteResultProps<T>.component3() = match
