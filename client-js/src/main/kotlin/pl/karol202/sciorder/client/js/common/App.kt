package pl.karol202.sciorder.client.js.common

import react.RBuilder
import react.dom.div
import react.dom.render
import kotlin.browser.document

fun main()
{
	document.addEventListener("DOMContentLoaded", {
		render(document.getElementById("root")) {
			app()
		}
	})
}

fun RBuilder.app() = div {
	+ "Reactive text"
}
