package pl.karol202.sciorder.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import pl.karol202.sciorder.viewmodel.ProductsViewModel

class ProductsFragment : Fragment()
{
	private val productsViewModel by lazy { ViewModelProviders.of(act).get<ProductsViewModel>() }

	private val adapter = ProductAdapter()

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
			inflater.inflate(R.layout.fragment_products, container, false)

	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		initRecycler()
		initRefreshLayout()

		observeProducts()
		observeState()
	}

	private fun initRecycler()
	{
		recyclerProducts.layoutManager = LinearLayoutManager(context)
		recyclerProducts.adapter = adapter
		recyclerProducts.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
	}

	private fun initRefreshLayout()
	{
		refreshLayoutProducts.setOnRefreshListener { productsViewModel.refreshProducts() }
	}

	private fun observeProducts()
	{
		productsViewModel.productsLiveData.observe(viewLifecycleOwner, Observer { productsList ->
			productsList?.let { adapter.products = it }
		})
	}

	private fun observeState()
	{
		productsViewModel.stateLiveData.observe(viewLifecycleOwner, Observer { state ->
			when(state)
			{
				is ProductsViewModel.State.Loaded -> refreshLayoutProducts.isRefreshing = false
				is ProductsViewModel.State.Loading -> refreshLayoutProducts.isRefreshing = true
				is ProductsViewModel.State.Error -> {
					refreshLayoutProducts.isRefreshing = false
					showErrorSnackbar()
				}
			}
		})
	}

	private fun showErrorSnackbar()
	{
		Snackbar.make(view ?: return, R.string.text_error, Snackbar.LENGTH_LONG).show()
	}
}