package pl.karol202.sciorder.client.android.admin.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_orders.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import pl.karol202.sciorder.client.android.admin.R
import pl.karol202.sciorder.client.android.admin.ui.adapter.OrderAdapter
import pl.karol202.sciorder.client.android.admin.ui.dialog.fragment.OrderFilterDialogFragment
import pl.karol202.sciorder.client.android.admin.ui.listener.OnOrderFilterSetListener
import pl.karol202.sciorder.client.android.common.component.FragmentWithMenu
import pl.karol202.sciorder.client.android.common.util.alertDialog
import pl.karol202.sciorder.client.android.common.util.ctx
import pl.karol202.sciorder.client.android.common.util.observeEvent
import pl.karol202.sciorder.client.android.common.util.showSnackbar
import pl.karol202.sciorder.client.android.common.viewmodel.AdminOrdersAndroidViewModel
import pl.karol202.sciorder.common.model.Order

class OrdersFragment : FragmentWithMenu(), OnOrderFilterSetListener
{
	private val ordersViewModel by sharedViewModel<AdminOrdersAndroidViewModel>()

	private val adapter = OrderAdapter { order, status -> ordersViewModel.updateOrderStatus(order, status) }

	override val layoutRes = R.layout.fragment_orders
	override val menuRes = R.menu.menu_fragment_orders

	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		initRefreshLayout()
		initRecycler()

		observeOrders()
		observeLoading()
		observeLoadingError()
		observeUpdateError()
	}

	private fun initRefreshLayout() = refreshLayoutOrders.setOnRefreshListener { ordersViewModel.refreshOrders() }

	private fun initRecycler()
	{
		recyclerOrders.layoutManager = LinearLayoutManager(context)
		recyclerOrders.adapter = adapter
	}

	private fun observeOrders() =
			ordersViewModel.ordersLiveData.observe(viewLifecycleOwner) { adapter.orders = it }

	private fun observeLoading() =
			ordersViewModel.loadingLiveData.observe(viewLifecycleOwner) { if(!it) refreshLayoutOrders.isRefreshing = false }

	private fun observeLoadingError() =
			ordersViewModel.loadingErrorEventLiveData.observeEvent(viewLifecycleOwner) { showSnackbar(R.string.text_loading_error) }

	private fun observeUpdateError() =
			ordersViewModel.updateErrorEventLiveData.observeEvent(viewLifecycleOwner) { showSnackbar(R.string.text_update_error) }

	override fun onMenuItemSelected(itemId: Int) = when(itemId)
	{
		R.id.item_orders_remove -> showOrdersRemoveDialog()
		R.id.item_order_filter -> showOrderFilterDialog()
		else -> null
	}

	private fun showOrdersRemoveDialog()
	{
		ctx.alertDialog {
			setTitle(R.string.dialog_orders_remove)
			setMessage(R.string.dialog_orders_remove_message)
			setPositiveButton(R.string.action_remove) { _, _ -> ordersViewModel.removeAllOrders() }
			setNegativeButton(R.string.action_cancel, null)
		}.show()
	}

	private fun showOrderFilterDialog() =
			OrderFilterDialogFragment.create(ordersViewModel.orderFilter, this).show(parentFragmentManager)

	override fun onOrderFilterSet(filter: Set<Order.Status>)
	{
		ordersViewModel.orderFilter = filter
	}
}
