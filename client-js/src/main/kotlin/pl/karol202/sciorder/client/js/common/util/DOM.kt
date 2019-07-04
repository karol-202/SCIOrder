package pl.karol202.sciorder.client.js.common.util

import org.w3c.dom.Document
import org.w3c.dom.events.Event

fun Document.addOnLoadListener(listener: (Event) -> Unit) = addEventListener("DOMContentLoaded", listener)
