package pl.karol202.sciorder.components

import android.os.Bundle
import pl.karol202.sciorder.extensions.set
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
