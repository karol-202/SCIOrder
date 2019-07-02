package pl.karol202.sciorder.client.js.common.util

import kotlin.properties.Delegates

object Delegates
{
	fun <T> observable(initialValue: T, listener: (T) -> Unit) =
			Delegates.observable(initialValue) { _, _, value -> listener(value) }

	fun <T> observable(initialValue: T, listeners: List<(T) -> Unit>) =
			observable(initialValue) { value -> listeners.invokeEach(value) }
}
