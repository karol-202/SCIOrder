package pl.karol202.sciorder.client.user.components

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

abstract class ExtendedDialogFragment : DialogFragment(),
                                        ComponentWithArguments
{
	override var componentArguments: Bundle?
		get() = arguments
		set(value) { arguments = value }

	fun show(fragmentManager: FragmentManager) = show(fragmentManager, javaClass.name)
}

abstract class ExtendedAlertDialog : AlertDialog,
                                     ComponentWithInstanceState
{
	override val instanceState = InstanceState()

	constructor(context: Context) : super(context)

	constructor(context: Context, themeResId: Int) : super(context, themeResId)

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
