package pl.karol202.sciorder.client.android.common.component

import android.os.Bundle
import pl.karol202.sciorder.client.android.common.util.BundleDelegate

interface ComponentWithArguments
{
	//Name 'arguments' clashes with Fragment's getArguments() and setArguments()
	var componentArguments: Bundle?

	fun <T : Any> arguments() =
		BundleDelegate.Nullable<T>(bundleProvider = this::getOrCreateNewArguments)

	fun <T : Any> arguments(defaultValue: T) =
		BundleDelegate.NotNull(bundleProvider = this::getOrCreateNewArguments,
		                       defaultValueProvider = { defaultValue })

	fun <T : Any> arguments(defaultValueProvider: () -> T) =
		BundleDelegate.NotNull(bundleProvider = this::getOrCreateNewArguments,
		                       defaultValueProvider = defaultValueProvider)

	fun <T : Any> argumentsOrThrow() =
		BundleDelegate.NotNull<T>(bundleProvider = this::getOrCreateNewArguments,
		                          defaultValueProvider = { throw IllegalStateException("No argument passed") })

	private fun getOrCreateNewArguments() = componentArguments ?: Bundle().also { componentArguments = it }
}
