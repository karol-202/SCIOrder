package pl.karol202.sciorder.client.js.common.util

import react.RComponent
import react.RProps
import kotlin.properties.Delegates
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun <T> observable(initialValue: T, listener: (T) -> Unit) =
		Delegates.observable(initialValue) { _, _, value -> listener(value) }

fun <T, P : RProps> RComponent<P, *>.prop(selector: P.() -> T) = object : ReadOnlyProperty<Any, T> {
	override fun getValue(thisRef: Any, property: KProperty<*>) =
			props.selector() ?: throw IllegalArgumentException("Prop not passed: ${property.name}")
}

fun <T, P : RProps> RComponent<P, *>.nullableProp(selector: P.() -> T?) = object : ReadOnlyProperty<Any, T?> {
	override fun getValue(thisRef: Any, property: KProperty<*>) = props.selector()
}
