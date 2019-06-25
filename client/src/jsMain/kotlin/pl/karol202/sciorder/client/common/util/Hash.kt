package pl.karol202.sciorder.client.common.util

import sha1.sha1

actual fun String.sha1() = sha1(this)