package pl.karol202.sciorder.client.user.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_order_track.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import pl.karol202.sciorder.client.common.extensions.observeEvent
import pl.karol202.sciorder.client.common.extensions.observeNonNull
import pl.karol202.sciorder.client.common.extensions.showSnackbar
import pl.karol202.sciorder.client.user.R
import pl.karol202.sciorder.client.user.ui.adapter.TrackedOrderAdapter
import pl.karol202.sciorder.client.user.viewmodel.OrderTrackViewModel
import pl.karol202.sciorder.client.user.viewmodel.ProductViewModel

class OrderTrackFragment : Fragment()
{
	private val productViewModel by sharedViewModel<ProductViewModel>()
	private val orderTrackViewModel by sharedViewModel<OrderTrackViewModel>()

	private val adapter = TrackedOrderAdapter { order -> orderTrackViewModel.removeOrder(order) }

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
			productViewModel.refreshProducts()
			orderTrackViewModel.refreshOrders()
		}
	}

	private fun initRecycler()
	{
		recyclerOrderTrack.layoutManager = LinearLayoutManager(context)
		recyclerOrderTrack.adapter = adapter
	}

	private fun observeOrders() =
			orderTrackViewModel.ordersLiveData.observeNonNull(viewLifecycleOwner) { adapter.items = it }

	private fun observeProducts() =
			productViewModel.productsLiveData.observeNonNull(viewLifecycleOwner) { adapter.products = it }

	private fun observeLoading() =
			orderTrackViewModel.loadingLiveData.observeNonNull(viewLifecycleOwner) { refreshLayoutOrderTrack.isRefreshing = it }

	private fun observeOrdersError() =
			orderTrackViewModel.errorEventLiveData.observeEvent(viewLifecycleOwner) { showSnackbar(R.string.text_loading_error) }
}
