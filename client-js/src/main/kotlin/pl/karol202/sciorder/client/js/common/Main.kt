package pl.karol202.sciorder.client.js.common

import pl.karol202.sciorder.client.js.common.component.appComponent
import pl.karol202.sciorder.client.js.common.util.addOnLoadListener
import react.dom.render
import kotlin.browser.document

fun main()
{
	document.addOnLoadListener {
		render(document.getElementById("root")) { appComponent() }
	}
}
