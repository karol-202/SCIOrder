package pl.karol202.sciorder.client.common.util

actual fun logVerbose(value: Any?) = console.log(value)
actual fun logDebug(value: Any?) = console.log(value)
actual fun logInfo(value: Any?) = console.info(value)
actual fun logWarning(value: Any?) = console.warn(value)
actual fun logError(value: Any?) = console.error(value)
