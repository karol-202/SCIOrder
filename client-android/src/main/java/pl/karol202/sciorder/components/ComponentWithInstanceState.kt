package pl.karol202.sciorder.components

import android.os.Bundle

interface ComponentWithInstanceState
{
	val instanceState: InstanceState

	fun <T : Any> instanceState() =
		BundleDelegate.Nullable<T>(bundleProvider = { instanceState.bundle })

	fun <T : Any> instanceStateOr(defaultValue: T) =
		BundleDelegate.NotNull(bundleProvider = { instanceState.bundle },
		                       defaultValueProvider = { defaultValue })

	fun <T : Any> instanceStateOr(defaultValueProvider: () -> T) =
		BundleDelegate.NotNull(bundleProvider = { instanceState.bundle },
		                       defaultValueProvider = defaultValueProvider)
}

class InstanceState
{
	companion object
	{
		private const val BUNDLE_NAME = "saved_instance_state"
	}

	val bundle = Bundle()

	fun onRestoreInstanceState(savedInstanceState: Bundle?)
	{
		savedInstanceState?.getBundle(BUNDLE_NAME)?.let {
			bundle.putAll(it)
		}
	}

	fun onSaveInstanceState(outState: Bundle)
	{
		outState.putBundle(BUNDLE_NAME, bundle)
	}
}
