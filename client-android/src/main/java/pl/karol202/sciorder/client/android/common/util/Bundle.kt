package pl.karol202.sciorder.client.android.common.util

import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

abstract class BundleDelegate<T>(private val bundleProvider: () -> Bundle) : ReadWriteProperty<Any?, T>
{
	class Nullable<T : Any>(bundleProvider: () -> Bundle) : BundleDelegate<T?>(bundleProvider)
	{
		override operator fun getValue(thisRef: Any?, property: KProperty<*>): T? = getFromBundle(property.name)
	}

	class NotNull<T : Any>(bundleProvider: () -> Bundle,
	                       private val defaultValueProvider: () -> T) : BundleDelegate<T>(bundleProvider)
	{
		override operator fun getValue(thisRef: Any?, property: KProperty<*>): T =
				getFromBundle(property.name) ?: defaultValueProvider().also { setValue(thisRef, property, it) }
	}

	@Suppress("UNCHECKED_CAST")
	protected fun getFromBundle(name: String) = bundleProvider()[name] as T?

	override operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T)
	{
		bundleProvider()[property.name] = value
	}
}

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
		else -> throw IllegalArgumentException("Type not supported: ${value.javaClass.name}.")
	}
}
