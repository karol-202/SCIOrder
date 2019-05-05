package pl.karol202.sciorder.client.admin.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_orders.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import pl.karol202.sciorder.client.admin.R
import pl.karol202.sciorder.client.admin.ui.adapter.OrderAdapter
import pl.karol202.sciorder.client.admin.viewmodel.OrderViewModel
import pl.karol202.sciorder.client.admin.viewmodel.ProductViewModel
import pl.karol202.sciorder.client.common.extensions.observeEvent
import pl.karol202.sciorder.client.common.extensions.observeNonNull
import pl.karol202.sciorder.client.common.extensions.showSnackbar

class OrdersFragment : Fragment()
{
	private val productViewModel by sharedViewModel<ProductViewModel>()
	private val orderViewModel by sharedViewModel<OrderViewModel>()

	private val adapter = OrderAdapter { order, status -> orderViewModel.updateOrderStatus(order, status) }

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
			orderViewModel.ordersLiveData.observeNonNull(viewLifecycleOwner) { adapter.items = it }

	private fun observeProducts() =
			productViewModel.productsLiveData.observeNonNull(viewLifecycleOwner) { adapter.products = it }

	private fun observeLoading() =
			orderViewModel.loadingLiveData.observeNonNull(viewLifecycleOwner) { refreshLayoutOrders.isRefreshing = it }

	private fun observeLoadingError() =
			orderViewModel.loadingErrorEventLiveData.observeEvent(viewLifecycleOwner) { showSnackbar(R.string.text_loading_error) }

	private fun observeUpdateError() =
			orderViewModel.updateErrorEventLiveData.observeEvent(viewLifecycleOwner) { showSnackbar(R.string.text_update_error) }
}
