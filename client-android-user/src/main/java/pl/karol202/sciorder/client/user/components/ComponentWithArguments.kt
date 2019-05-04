package pl.karol202.sciorder.client.user.components

import android.os.Bundle

interface ComponentWithArguments
{
	//Name 'arguments' clashes with Fragment's getArguments() and setArguments()
	var componentArguments: Bundle?

	fun <T : Any> arguments() =
		BundleDelegate.Nullable<T>(bundleProvider = this::getOrCreateNewArguments)

	fun <T : Any> argumentsOr(defaultValue: T) =
		BundleDelegate.NotNull(bundleProvider = this::getOrCreateNewArguments,
		                       defaultValueProvider = { defaultValue })

	fun <T : Any> argumentsOr(defaultValueProvider: () -> T) =
		BundleDelegate.NotNull(bundleProvider = this::getOrCreateNewArguments,
		                       defaultValueProvider = defaultValueProvider)

	fun <T : Any> argumentsOrThrow() =
		BundleDelegate.NotNull<T>(bundleProvider = this::getOrCreateNewArguments,
		                          defaultValueProvider = { throw IllegalStateException("No argument passed") })

	private fun getOrCreateNewArguments() = componentArguments ?: Bundle().also { componentArguments = it }
}
