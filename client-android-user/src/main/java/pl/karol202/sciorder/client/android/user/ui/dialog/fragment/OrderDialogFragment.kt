package pl.karol202.sciorder.client.android.user.ui.dialog.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import pl.karol202.sciorder.client.android.common.components.ExtendedDialogFragment
import pl.karol202.sciorder.client.android.common.extensions.ctx
import pl.karol202.sciorder.client.android.common.extensions.setArguments
import pl.karol202.sciorder.client.android.common.extensions.setTargetFragment
import pl.karol202.sciorder.client.android.common.extensions.to
import pl.karol202.sciorder.client.android.user.ui.dialog.OrderDialog
import pl.karol202.sciorder.client.android.user.ui.listener.OnOrderDetailsSetListener

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
