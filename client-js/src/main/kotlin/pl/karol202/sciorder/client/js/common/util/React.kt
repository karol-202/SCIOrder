package pl.karol202.sciorder.client.js.common.util

import react.RBuilder
import react.RComponent
import react.RElementBuilder
import react.RProps
import react.ReactElement
import react.router.dom.RouteResultProps
import react.router.dom.redirect
import react.router.dom.route
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

fun <T, P : RProps> RComponent<P, *>.prop(selector: P.() -> T) = object : ReadOnlyProperty<Any, T>
{
	override fun getValue(thisRef: Any, property: KProperty<*>) =
			props.selector() ?: throw IllegalArgumentException("Prop not passed: ${property.name}")
}

fun <T, P : RProps> RComponent<P, *>.nullableProp(selector: P.() -> T?) = object : ReadOnlyProperty<Any, T?>
{
	override fun getValue(thisRef: Any, property: KProperty<*>) = props.selector()
}

fun RBuilder.routeElse(render: (RouteResultProps<RProps>) -> ReactElement?) = route("", render = render)

fun RBuilder.routeElse(component: KClass<out RComponent<*, *>>) = route("", component)

fun RBuilder.redirectTo(to: String) = redirect(from = "", to = to)

operator fun <T : RProps> RouteResultProps<T>.component1() = history

operator fun <T : RProps> RouteResultProps<T>.component2() = location

operator fun <T : RProps> RouteResultProps<T>.component3() = match

fun RElementBuilder<*>.onEnterEventListener(onEnter: () -> Unit)
{
	attrs.asDynamic().onKeyDown = { event: dynamic -> if(event.key == "Enter") onEnter() }
}
