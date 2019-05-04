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
import pl.karol202.sciorder.client.user.R
import pl.karol202.sciorder.client.user.ui.adapters.TrackedOrderAdapter
import pl.karol202.sciorder.client.user.viewmodel.OrderTrackViewModel
import pl.karol202.sciorder.client.user.viewmodel.ProductViewModel

class OrderTrackFragment : Fragment()
{
	private val productViewModel by sharedViewModel<ProductViewModel>()
	private val orderTrackViewModel by sharedViewModel<OrderTrackViewModel>()

	private val adapter = TrackedOrderAdapter(productSupplier = { id -> productViewModel.findProductById(id) },
	                                          orderRemoveListener = { order -> orderTrackViewModel.removeOrder(order) })

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
		inflater.inflate(R.layout.fragment_order_track, container, false)

	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		initRefreshLayout()
		initRecycler()

		observeOrders()
		observeLoading()
		observeOrdersError()
	}

	private fun initRefreshLayout()
	{
		refreshLayoutOrderTrack.setOnRefreshListener { orderTrackViewModel.refreshOrders() }
	}

	private fun initRecycler()
	{
		recyclerOrderTrack.layoutManager = LinearLayoutManager(context)
		recyclerOrderTrack.adapter = adapter
	}

	private fun observeOrders()
	{
		orderTrackViewModel.ordersLiveData.observe(viewLifecycleOwner, Observer { orders ->
			orders?.let { adapter.orders = it }
		})
	}

	private fun observeLoading()
	{
		orderTrackViewModel.loadingLiveData.observe(viewLifecycleOwner, Observer { loading ->
			refreshLayoutOrderTrack.isRefreshing = loading
		})
	}

	private fun observeOrdersError()
	{
		orderTrackViewModel.errorEventLiveData.observe(viewLifecycleOwner, Observer { event ->
			if(event.getIfNotConsumed() == Unit) showErrorSnackbar(R.string.text_loading_error)
		})
	}

	private fun showErrorSnackbar(@StringRes message: Int)
	{
		Snackbar.make(view ?: return, message, Snackbar.LENGTH_LONG).show()
	}
}
