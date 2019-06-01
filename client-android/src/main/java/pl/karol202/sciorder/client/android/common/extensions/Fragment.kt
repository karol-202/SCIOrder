package pl.karol202.sciorder.client.android.common.extensions

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import kotlin.reflect.KProperty

val Fragment.ctx get() = requireContext()

data class FragmentArgument<T>(val key: String,
                               val value: T?)

infix fun <T> KProperty<T>.to(value: T?) = FragmentArgument(this.name, value)

fun <F : Fragment> F.setArguments(vararg args: FragmentArgument<*>) = apply {
	val bundle = Bundle()
	args.forEach { bundle[it.key] = it.value }
	arguments = bundle
}

fun <F : Fragment> F.setTargetFragment(fragment: Fragment) = apply {
	setTargetFragment(fragment, -1)
}

fun Fragment.showSnackbar(@StringRes message: Int, duration: Int = Snackbar.LENGTH_LONG, block: Snackbar.() -> Unit = {})
{
	Snackbar.make(view ?: return, message, duration).apply(block).show()
}
