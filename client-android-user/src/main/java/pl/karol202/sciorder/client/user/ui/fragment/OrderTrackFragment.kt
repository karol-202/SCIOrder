package pl.karol202.sciorder.client.user.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_order_track.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import pl.karol202.sciorder.client.common.extensions.observeEvent
import pl.karol202.sciorder.client.common.extensions.observeNonNull
import pl.karol202.sciorder.client.common.extensions.showSnackbar
import pl.karol202.sciorder.client.user.R
import pl.karol202.sciorder.client.user.ui.adapter.TrackedOrderAdapter
import pl.karol202.sciorder.client.user.viewmodel.OrdersTrackViewModel
import pl.karol202.sciorder.client.user.viewmodel.ProductsViewModel

class OrderTrackFragment : Fragment()
{
	private val productsViewModel by sharedViewModel<ProductsViewModel>()
	private val ordersTrackViewModel by sharedViewModel<OrdersTrackViewModel>()

	private val adapter = TrackedOrderAdapter { order -> ordersTrackViewModel.removeOrder(order) }

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
		inflater.inflate(R.layout.fragment_order_track, container, false)

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
			ordersTrackViewModel.loadingLiveData.observeNonNull(viewLifecycleOwner) { refreshLayoutOrderTrack.isRefreshing = it }

	private fun observeOrdersError() =
			ordersTrackViewModel.errorEventLiveData.observeEvent(viewLifecycleOwner) { showSnackbar(R.string.text_loading_error) }
}
