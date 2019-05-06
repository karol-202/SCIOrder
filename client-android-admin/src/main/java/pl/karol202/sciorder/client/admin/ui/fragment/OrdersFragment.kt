package pl.karol202.sciorder.client.admin.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_orders.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import pl.karol202.sciorder.client.admin.R
import pl.karol202.sciorder.client.admin.components.FragmentWithMenu
import pl.karol202.sciorder.client.admin.ui.adapter.OrderAdapter
import pl.karol202.sciorder.client.admin.ui.dialog.fragment.OrderFilterDialogFragment
import pl.karol202.sciorder.client.admin.ui.listener.OnOrderFilterSetListener
import pl.karol202.sciorder.client.admin.viewmodel.OrderViewModel
import pl.karol202.sciorder.client.admin.viewmodel.ProductViewModel
import pl.karol202.sciorder.client.common.extensions.*
import pl.karol202.sciorder.common.model.Order

class OrdersFragment : FragmentWithMenu(), OnOrderFilterSetListener
{
	private val productViewModel by sharedViewModel<ProductViewModel>()
	private val orderViewModel by sharedViewModel<OrderViewModel>()

	private val adapter = OrderAdapter { order, status -> orderViewModel.updateOrderStatus(order, status) }

	override val menuRes = R.menu.menu_fragment_orders

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
		inflater.inflate(R.layout.fragment_orders, container, false)

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
			productViewModel.refreshProducts()
			orderViewModel.refreshOrders()
		}
	}

	private fun initRecycler()
	{
		recyclerOrders.layoutManager = LinearLayoutManager(context)
		recyclerOrders.adapter = adapter
	}

	private fun observeOrders() =
			orderViewModel.ordersLiveData.observeNonNull(viewLifecycleOwner) { adapter.orders = it }

	private fun observeProducts() =
			productViewModel.productsLiveData.observeNonNull(viewLifecycleOwner) { adapter.products = it }

	private fun observeLoading() =
			orderViewModel.loadingLiveData.observeNonNull(viewLifecycleOwner) { refreshLayoutOrders.isRefreshing = it }

	private fun observeLoadingError() =
			orderViewModel.loadingErrorEventLiveData.observeEvent(viewLifecycleOwner) { showSnackbar(R.string.text_loading_error) }

	private fun observeUpdateError() =
			orderViewModel.updateErrorEventLiveData.observeEvent(viewLifecycleOwner) { showSnackbar(R.string.text_update_error) }

	private fun observeOrderFilter() =
			orderViewModel.orderFilterLiveData.observeNonNull(viewLifecycleOwner) { adapter.orderFilter = it }

	private fun showOrderFilterDialog() =
			OrderFilterDialogFragment.create(orderViewModel.orderFilter, this).show(fragmentManager)

	override fun onOrderFilterSet(filter: Set<Order.Status>)
	{
		orderViewModel.orderFilter = filter
	}

	override fun onMenuItemSelected(itemId: Int) = when(itemId)
	{
		R.id.item_order_filter -> showOrderFilterDialog()
		else -> null
	}
}
