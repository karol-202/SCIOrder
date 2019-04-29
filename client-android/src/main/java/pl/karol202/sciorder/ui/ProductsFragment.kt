package pl.karol202.sciorder.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_products.*
import pl.karol202.sciorder.R
import pl.karol202.sciorder.extensions.act
import pl.karol202.sciorder.model.Product
import pl.karol202.sciorder.viewmodel.OrderViewModel
import pl.karol202.sciorder.viewmodel.ProductViewModel

class ProductsFragment : Fragment(), OnProductOrderListener
{
	private val productViewModel by lazy { ViewModelProviders.of(act).get<ProductViewModel>() }
	private val orderViewModel by lazy { ViewModelProviders.of(act).get<OrderViewModel>() }

	private val adapter = ProductAdapter().apply {
		onProductSelectListener = { product -> showProductOrderDialog(product) }
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
			inflater.inflate(R.layout.fragment_products, container, false)

	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		initRecycler()
		initRefreshLayout()

		observeProducts()
		observeLoading()
		observeProductError()
		observeOrderError()
	}

	private fun initRecycler()
	{
		recyclerProducts.layoutManager = LinearLayoutManager(context)
		recyclerProducts.adapter = adapter
		recyclerProducts.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
	}

	private fun initRefreshLayout()
	{
		refreshLayoutProducts.setOnRefreshListener { productViewModel.refreshProducts() }
	}

	private fun observeProducts()
	{
		productViewModel.productsLiveData.observe(viewLifecycleOwner, Observer { productsList ->
			productsList?.let { adapter.products = it }
		})
	}

	private fun observeLoading()
	{
		productViewModel.loadingLiveData.observe(viewLifecycleOwner, Observer { loading ->
			refreshLayoutProducts.isRefreshing = loading
		})
	}

	private fun observeProductError()
	{
		productViewModel.errorEventLiveData.observe(viewLifecycleOwner, Observer { event ->
			if(event.getIfNotConsumed() == Unit) showErrorSnackbar(R.string.text_products_loading_error)
		})
	}

	private fun observeOrderError()
	{
		orderViewModel.errorEventLiveData.observe(viewLifecycleOwner, Observer { event ->
			if(event.getIfNotConsumed() == Unit) showErrorSnackbar(R.string.text_order_error)
		})
	}

	private fun showErrorSnackbar(@StringRes message: Int)
	{
		Snackbar.make(view ?: return, message, Snackbar.LENGTH_LONG).show()
	}

	private fun showProductOrderDialog(product: Product) =
			fragmentManager?.let { ProductOrderDialogFragment.create(product, this).show(it) }

	override fun onProductOrder(product: Product, quantity: Int, parameters: Map<String, String>) =
			orderViewModel.orderProduct(product, quantity, parameters)
}