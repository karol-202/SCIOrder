package pl.karol202.sciorder.extensions

import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable

operator fun Bundle.set(key: String, value: Any?)
{
	when(value)
	{
		null -> remove(key)
		is Boolean -> putBoolean(key, value)
		is Byte -> putByte(key, value)
		is Char -> putChar(key, value)
		is Double -> putDouble(key, value)
		is Float -> putFloat(key, value)
		is Int -> putInt(key, value)
		is Long -> putLong(key, value)
		is Short -> putShort(key, value)
		is String -> putString(key, value)
		is Bundle -> putBundle(key, value)
		is Parcelable -> putParcelable(key, value)
		is Serializable -> putSerializable(key, value)
		else -> throw IllegalArgumentException("Type not supported.")
	}
}