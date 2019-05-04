package pl.karol202.sciorder.client.user.ui.dialog.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import pl.karol202.sciorder.client.user.components.ExtendedDialogFragment
import pl.karol202.sciorder.client.user.extensions.ctx
import pl.karol202.sciorder.client.user.extensions.setArguments
import pl.karol202.sciorder.client.user.extensions.setTargetFragment
import pl.karol202.sciorder.client.user.extensions.to
import pl.karol202.sciorder.client.user.ui.listeners.OnOrderDetailsSetListener
import pl.karol202.sciorder.client.user.ui.dialog.OrderDialog

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
