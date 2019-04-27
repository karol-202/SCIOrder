package pl.karol202.sciorder.server.util

import org.litote.kmongo.newId

fun <T> newStringId() = newId<T>().toString()