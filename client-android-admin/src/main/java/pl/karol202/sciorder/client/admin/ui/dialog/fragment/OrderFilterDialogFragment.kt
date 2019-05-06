package pl.karol202.sciorder.client.admin.ui.dialog.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import pl.karol202.sciorder.client.admin.ui.dialog.OrderFilterDialog
import pl.karol202.sciorder.client.admin.ui.listener.OnOrderFilterSetListener
import pl.karol202.sciorder.client.common.components.ExtendedDialogFragment
import pl.karol202.sciorder.client.common.extensions.ctx
import pl.karol202.sciorder.client.common.extensions.setArguments
import pl.karol202.sciorder.client.common.extensions.setTargetFragment
import pl.karol202.sciorder.client.common.extensions.to
import pl.karol202.sciorder.common.model.Order

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