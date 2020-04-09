package pl.karol202.sciorder.client.common.util

import android.util.Log

private const val TAG = "pl.karol202.sciorder"

actual fun logVerbose(value: Any?)
{
	Log.v(TAG, value.toString())
}

actual fun logDebug(value: Any?)
{
	Log.d(TAG, value.toString())
}

actual fun logInfo(value: Any?)
{
	Log.i(TAG, value.toString())
}

actual fun logWarning(value: Any?)
{
	Log.w(TAG, value.toString())
}

actual fun logError(value: Any?)
{
	Log.e(TAG, value.toString())
}
