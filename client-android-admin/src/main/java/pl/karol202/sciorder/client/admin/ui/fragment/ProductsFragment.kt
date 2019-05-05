package pl.karol202.sciorder.client.admin.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_products.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import pl.karol202.sciorder.client.admin.R
import pl.karol202.sciorder.client.admin.ui.adapter.ProductAdapter
import pl.karol202.sciorder.client.admin.viewmodel.ProductViewModel
import pl.karol202.sciorder.client.common.extensions.observeEvent
import pl.karol202.sciorder.client.common.extensions.observeNonNull
import pl.karol202.sciorder.client.common.extensions.showSnackbar

class ProductsFragment : Fragment()
{
	private val productViewModel by sharedViewModel<ProductViewModel>()

	private val adapter = ProductAdapter()

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
			inflater.inflate(R.layout.fragment_products, container, false)

	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		initRefreshLayout()
		initRecycler()

		observeProducts()
		observeLoading()
		observeProductError()
	}

	private fun initRefreshLayout()
	{
		refreshLayoutProducts.setOnRefreshListener { productViewModel.refreshProducts() }
	}

	private fun initRecycler()
	{
		recyclerProducts.layoutManager = LinearLayoutManager(context)
		recyclerProducts.adapter = adapter
	}

	private fun observeProducts() =
			productViewModel.productsLiveData.observeNonNull(viewLifecycleOwner) { adapter.items = it }

	private fun observeLoading() =
			productViewModel.loadingLiveData.observeNonNull(viewLifecycleOwner) { refreshLayoutProducts.isRefreshing = it }

	private fun observeProductError() =
			productViewModel.errorEventLiveData.observeEvent(viewLifecycleOwner) { showSnackbar(R.string.text_loading_error) }
}
