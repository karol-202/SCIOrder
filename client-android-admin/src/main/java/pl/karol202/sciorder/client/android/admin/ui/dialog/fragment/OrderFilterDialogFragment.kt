package pl.karol202.sciorder.client.android.admin.ui.dialog.fragment

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import pl.karol202.sciorder.client.android.admin.ui.dialog.OrderFilterDialog
import pl.karol202.sciorder.client.android.admin.ui.listener.OnOrderFilterSetListener
import pl.karol202.sciorder.client.android.common.util.*
import pl.karol202.sciorder.common.model.Order

class OrderFilterDialogFragment : DialogFragment()
{
	companion object
	{
		fun <L> create(filter: Set<Order.Status>, filterSetListener: L)
				where L : Fragment,
				      L : OnOrderFilterSetListener =
				OrderFilterDialogFragment()
						.setArguments(OrderFilterDialogFragment::filter set filter)
						.setTargetFragment(filterSetListener)
	}

	private val filter by argumentsOrThrow<Set<Order.Status>>()
	private val filterSetListener by lazy { targetFragment as OnOrderFilterSetListener }

	override fun onCreateDialog(savedInstanceState: Bundle?) =
			OrderFilterDialog(ctx, filter) { filterSetListener.onOrderFilterSet(it) }
}
