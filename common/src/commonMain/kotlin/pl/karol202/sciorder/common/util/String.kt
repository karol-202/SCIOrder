package pl.karol202.sciorder.common.util

fun String.isValidInt() = toIntOrNull() != null

fun String.isValidFloat() = toFloatOrNull() != null
