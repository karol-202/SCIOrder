package pl.karol202.sciorder.client.android.user.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_order_track.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import pl.karol202.sciorder.client.android.common.component.InflatedFragment
import pl.karol202.sciorder.client.android.common.extension.observeEvent
import pl.karol202.sciorder.client.android.common.extension.observeNonNull
import pl.karol202.sciorder.client.android.common.extension.showSnackbar
import pl.karol202.sciorder.client.android.user.R
import pl.karol202.sciorder.client.android.user.ui.adapter.TrackedOrderAdapter
import pl.karol202.sciorder.client.android.user.viewmodel.OrdersTrackViewModel
import pl.karol202.sciorder.client.android.user.viewmodel.ProductsViewModel

class OrderTrackFragment : InflatedFragment()
{
	private val productsViewModel by sharedViewModel<ProductsViewModel>()
	private val ordersTrackViewModel by sharedViewModel<OrdersTrackViewModel>()

	private val adapter = TrackedOrderAdapter { order -> ordersTrackViewModel.removeOrder(order) }

	override val layoutRes = R.layout.fragment_order_track

	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		initRefreshLayout()
		initRecycler()

		observeOrders()
		observeProducts()
		observeLoading()
		observeOrdersError()
	}

	private fun initRefreshLayout()
	{
		refreshLayoutOrderTrack.setOnRefreshListener {
			productsViewModel.refreshProducts()
			ordersTrackViewModel.refreshOrders()
		}
	}

	private fun initRecycler()
	{
		recyclerOrderTrack.layoutManager = LinearLayoutManager(context)
		recyclerOrderTrack.adapter = adapter
	}

	private fun observeOrders() =
			ordersTrackViewModel.ordersLiveData.observeNonNull(viewLifecycleOwner) { adapter.orders = it }

	private fun observeProducts() =
			productsViewModel.productsLiveData.observeNonNull(viewLifecycleOwner) { adapter.products = it }

	private fun observeLoading() =
			ordersTrackViewModel.loadingLiveData.observeNonNull(viewLifecycleOwner) { if(!it) refreshLayoutOrderTrack.isRefreshing = false }

	private fun observeOrdersError() =
			ordersTrackViewModel.errorEventLiveData.observeEvent(viewLifecycleOwner) { showSnackbar(R.string.text_loading_error) }
}
