package pl.karol202.sciorder.client.android.admin.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_orders.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import pl.karol202.sciorder.client.android.admin.R
import pl.karol202.sciorder.client.android.admin.ui.adapter.OrderAdapter
import pl.karol202.sciorder.client.android.admin.ui.dialog.fragment.OrderFilterDialogFragment
import pl.karol202.sciorder.client.android.admin.ui.listener.OnOrderFilterSetListener
import pl.karol202.sciorder.client.android.admin.viewmodel.OrdersViewModel
import pl.karol202.sciorder.client.android.admin.viewmodel.ProductsViewModel
import pl.karol202.sciorder.client.android.common.component.FragmentWithMenu
import pl.karol202.sciorder.client.android.common.extension.*
import pl.karol202.sciorder.common.Order

class OrdersFragment : FragmentWithMenu(), OnOrderFilterSetListener
{
	private val productsViewModel by sharedViewModel<ProductsViewModel>()
	private val ordersViewModel by sharedViewModel<OrdersViewModel>()

	private val adapter = OrderAdapter { order, status -> ordersViewModel.updateOrderStatus(order, status) }

	override val layoutRes = R.layout.fragment_orders
	override val menuRes = R.menu.menu_fragment_orders

	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		initRefreshLayout()
		initRecycler()

		observeOrders()
		observeProducts()
		observeLoading()
		observeLoadingError()
		observeUpdateError()
		observeOrderFilter()
	}

	private fun initRefreshLayout()
	{
		refreshLayoutOrders.setOnRefreshListener {
			productsViewModel.refreshProducts()
			ordersViewModel.refreshOrders()
		}
	}

	private fun initRecycler()
	{
		recyclerOrders.layoutManager = LinearLayoutManager(context)
		recyclerOrders.adapter = adapter
	}

	private fun observeOrders() =
			ordersViewModel.ordersLiveData.observeNonNull(viewLifecycleOwner) { adapter.orders = it }

	private fun observeProducts() =
			productsViewModel.productsLiveData.observeNonNull(viewLifecycleOwner) { adapter.products = it }

	private fun observeLoading() =
			ordersViewModel.loadingLiveData.observeNonNull(viewLifecycleOwner) { if(!it) refreshLayoutOrders.isRefreshing = false }

	private fun observeLoadingError() =
			ordersViewModel.loadingErrorEventLiveData.observeEvent(viewLifecycleOwner) { showSnackbar(R.string.text_loading_error) }

	private fun observeUpdateError() =
			ordersViewModel.updateErrorEventLiveData.observeEvent(viewLifecycleOwner) { showSnackbar(R.string.text_update_error) }

	private fun observeOrderFilter() =
			ordersViewModel.orderFilterLiveData.observeNonNull(viewLifecycleOwner) { adapter.orderFilter = it }

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
			OrderFilterDialogFragment.create(ordersViewModel.orderFilter, this).show(fragmentManager)

	override fun onOrderFilterSet(filter: Set<Order.Status>)
	{
		ordersViewModel.orderFilter = filter
	}
}
