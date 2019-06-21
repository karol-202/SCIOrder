@file:JvmName("TimeAndroidKt")

package pl.karol202.sciorder.client.common.extensions

import kotlin.jvm.JvmName

val Int.millis get() = toLong()
val Int.seconds get() = 1000 * millis
val Int.minutes get() = 60 * seconds
val Int.hours get() = 60 * minutes
val Int.days get() = 24 * hours

expect fun currentTimeMillis(): Long
