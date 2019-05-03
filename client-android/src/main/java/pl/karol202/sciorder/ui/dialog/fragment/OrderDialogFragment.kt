package pl.karol202.sciorder.ui.dialog.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import pl.karol202.sciorder.components.ExtendedDialogFragment
import pl.karol202.sciorder.extensions.ctx
import pl.karol202.sciorder.extensions.setArguments
import pl.karol202.sciorder.extensions.setTargetFragment
import pl.karol202.sciorder.extensions.to
import pl.karol202.sciorder.ui.listeners.OnOrderDetailsSetListener
import pl.karol202.sciorder.ui.dialog.OrderDialog

class OrderDialogFragment : ExtendedDialogFragment()
{
	companion object
	{
		fun <L> create(case: OnOrderDetailsSetListener.Case, orderListener: L)
				where L : Fragment,
				      L : OnOrderDetailsSetListener =
			OrderDialogFragment().setArguments(OrderDialogFragment::case to case)
					.setTargetFragment(orderListener)
	}

	private val case by argumentsOrThrow<OnOrderDetailsSetListener.Case>()
	private val orderListener by lazy { targetFragment as OnOrderDetailsSetListener }

	override fun onCreateDialog(savedInstanceState: Bundle?) = OrderDialog(ctx, case, orderListener)
}
