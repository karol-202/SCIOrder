package pl.karol202.sciorder.client.common.components

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

abstract class ExtendedFragment : InflatedFragment(),
                                  ComponentWithInstanceState,
                                  ComponentWithArguments
{
	override var componentArguments: Bundle?
		get() = arguments
		set(value) { arguments = value }

	override val instanceState = InstanceState()

	override fun onViewStateRestored(savedInstanceState: Bundle?)
	{
		super.onViewStateRestored(savedInstanceState)
		instanceState.onRestoreInstanceState(savedInstanceState)
		onRestoreInstanceState()
	}

	open fun onRestoreInstanceState() { }

	override fun onSaveInstanceState(outState: Bundle)
	{
		instanceState.onSaveInstanceState(outState)
	}
}

abstract class ExtendedDialogFragment : DialogFragment(),
                                        ComponentWithArguments
{
	override var componentArguments: Bundle?
		get() = arguments
		set(value) { arguments = value }

	fun show(fragmentManager: FragmentManager?)
	{
		fragmentManager?.let { show(it, javaClass.name) }
	}
}

abstract class ExtendedAlertDialog(context: Context) : AlertDialog(context),
                                                       ComponentWithInstanceState
{
	override val instanceState = InstanceState()

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)

		instanceState.onRestoreInstanceState(savedInstanceState)
	}

	override fun onSaveInstanceState(): Bundle
	{
		val outState = super.onSaveInstanceState()
		instanceState.onSaveInstanceState(outState)
		return outState
	}
}
