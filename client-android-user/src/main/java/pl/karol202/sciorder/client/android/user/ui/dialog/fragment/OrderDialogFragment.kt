package pl.karol202.sciorder.client.android.user.ui.dialog.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import pl.karol202.sciorder.client.android.common.component.ExtendedDialogFragment
import pl.karol202.sciorder.client.android.common.extension.ctx
import pl.karol202.sciorder.client.android.common.extension.setArguments
import pl.karol202.sciorder.client.android.common.extension.setTargetFragment
import pl.karol202.sciorder.client.android.common.extension.to
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
