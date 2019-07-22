package pl.karol202.sciorder.client.android.common.component

import android.os.Bundle
import pl.karol202.sciorder.client.android.common.util.BundleDelegate

interface ComponentWithInstanceState
{
	val instanceState: InstanceState

	fun <T : Any> instanceState() =
		BundleDelegate.Nullable<T>(bundleProvider = { instanceState.bundle })

	fun <T : Any> instanceState(defaultValue: T) =
		BundleDelegate.NotNull(bundleProvider = { instanceState.bundle },
		                       defaultValueProvider = { defaultValue })

	fun <T : Any> instanceState(defaultValueProvider: () -> T) =
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
