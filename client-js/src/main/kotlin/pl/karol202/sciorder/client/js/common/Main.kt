package pl.karol202.sciorder.client.js.common

import org.w3c.dom.Document
import org.w3c.dom.events.Event
import react.dom.render
import kotlin.browser.document

fun main()
{
	document.addOnLoadListener {
		render(document.getElementById("root")) {

		}
	}
}

private fun Document.addOnLoadListener(listener: (Event) -> Unit) = addEventListener("DOMContentLoaded", listener)
