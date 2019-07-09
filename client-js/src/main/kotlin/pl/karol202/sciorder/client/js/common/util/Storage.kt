package pl.karol202.sciorder.client.js.common.util

import org.w3c.dom.get
import org.w3c.dom.set
import kotlin.browser.localStorage

object Storage
{
	const val OWNER_ID = "owner_id"
	const val OWNER_NAME = "owner_name"
	const val OWNER_HASH = "owner_hash"

	operator fun get(key: String) = localStorage[key]

	operator fun set(key: String, value: String?) =
			if(value != null) localStorage[key] = value
			else localStorage.removeItem(key)
}
