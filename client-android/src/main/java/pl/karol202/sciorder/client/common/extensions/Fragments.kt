package pl.karol202.sciorder.client.common.extensions

import android.os.Bundle
import androidx.fragment.app.Fragment
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
