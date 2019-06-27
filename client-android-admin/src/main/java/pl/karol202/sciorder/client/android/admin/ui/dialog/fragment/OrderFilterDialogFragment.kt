package pl.karol202.sciorder.client.android.admin.ui.dialog.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import pl.karol202.sciorder.client.android.admin.ui.dialog.OrderFilterDialog
import pl.karol202.sciorder.client.android.admin.ui.listener.OnOrderFilterSetListener
import pl.karol202.sciorder.client.android.common.component.ExtendedDialogFragment
import pl.karol202.sciorder.client.android.common.util.ctx
import pl.karol202.sciorder.client.android.common.util.setArguments
import pl.karol202.sciorder.client.android.common.util.setTargetFragment
import pl.karol202.sciorder.client.android.common.util.to
import pl.karol202.sciorder.common.Order

class OrderFilterDialogFragment : ExtendedDialogFragment()
{
	companion object
	{
		fun <L> create(filter: Set<Order.Status>, filterSetListener: L)
				where L : Fragment,
				      L : OnOrderFilterSetListener =
				OrderFilterDialogFragment().setArguments(OrderFilterDialogFragment::filter to filter)
						.setTargetFragment(filterSetListener)
	}

	private val filter by argumentsOrThrow<Set<Order.Status>>()
	private val filterSetListener by lazy { targetFragment as OnOrderFilterSetListener }

	override fun onCreateDialog(savedInstanceState: Bundle?) = OrderFilterDialog(ctx, filter, filterSetListener)
}
