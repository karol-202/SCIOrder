package pl.karol202.sciorder.common.util

import kotlin.math.floor

fun String.isValidInt() = toIntOrNull() != null

fun String.isValidFloat() = toFloatOrNull() != null

fun Float.isValidInt() = floor(this) == this
