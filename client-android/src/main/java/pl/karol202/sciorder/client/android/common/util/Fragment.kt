package pl.karol202.sciorder.client.android.common.util

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.google.android.material.snackbar.Snackbar
import kotlin.reflect.KProperty

val Fragment.ctx get() = requireContext()

data class FragmentArgument<T>(val key: String,
                               val value: T?)

infix fun <T> KProperty<T>.set(value: T?) = FragmentArgument(this.name, value)

fun <F : Fragment> F.setArguments(vararg args: FragmentArgument<*>) = apply {
	val bundle = Bundle()
	args.forEach { bundle[it.key] = it.value }
	arguments = bundle
}

fun <T : Any> Fragment.arguments() =
		BundleDelegate.Nullable<T>(arguments)

fun <T : Any> Fragment.arguments(defaultValue: T) =
		BundleDelegate.NotNull(arguments) { defaultValue }

fun <T : Any> Fragment.arguments(defaultValueProvider: () -> T) =
		BundleDelegate.NotNull(arguments, defaultValueProvider)

fun <T : Any> Fragment.argumentsOrThrow() =
		BundleDelegate.NotNull<T>(arguments) { throw IllegalStateException("No argument passed") }

fun <F : Fragment> F.setTargetFragment(fragment: Fragment) = apply {
	setTargetFragment(fragment, -1)
}

fun Fragment.showSnackbar(@StringRes message: Int, duration: Int = Snackbar.LENGTH_LONG, block: Snackbar.() -> Unit = {})
{
	Snackbar.make(view ?: return, message, duration).apply(block).show()
}

fun DialogFragment.show(fragmentManager: FragmentManager?)
{
	show(fragmentManager ?: return, javaClass.name)
}

fun sharedElements(vararg views: View) = FragmentNavigatorExtras(*views.map { it to it.transitionName }.toTypedArray())
