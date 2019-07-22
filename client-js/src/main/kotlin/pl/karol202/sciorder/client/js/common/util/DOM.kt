package pl.karol202.sciorder.client.js.common.util

import kotlinx.html.HTMLTag
import kotlinx.html.TagConsumer
import kotlinx.html.attributes.StringAttribute
import kotlinx.html.attributesMapOf
import org.w3c.dom.Document
import org.w3c.dom.events.Event
import react.RBuilder
import react.dom.RDOMBuilder
import react.dom.tag

fun Document.addOnLoadListener(listener: (Event) -> Unit) = addEventListener("DOMContentLoaded", listener)

private val stringAttribute = StringAttribute()

inline fun RBuilder.path(d: String? = null, classes: String? = null, block: RDOMBuilder<PATH>.() -> Unit) =
		tag(block) { PATH(attributesMapOf("d", d, "class", classes), it) }

open class PATH(initialAttributes: Map<String, String>,
                override val consumer: TagConsumer<*>) : HTMLTag("path", consumer, initialAttributes,
                                                                 "http://www.w3.org/2000/svg", false, true)
{
	var d: String
		get() = stringAttribute[this, "d"]
		set(value) { stringAttribute[this, "d"] = value }
	
	var pathLength: String
		get() = stringAttribute[this, "pathLength"]
		set(value) { stringAttribute[this, "pathLength"] = value }
	
	var fillOpacity: String
		get() = stringAttribute[this, "fillOpacity"]
		set(value) { stringAttribute[this, "fillOpacity"] = value }
}
