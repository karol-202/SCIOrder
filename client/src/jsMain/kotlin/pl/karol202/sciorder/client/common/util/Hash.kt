package pl.karol202.sciorder.client.common.util

actual fun String.sha1() = sha1(this)

@JsModule("sha1")
private external fun sha1(value: String): String
