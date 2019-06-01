package pl.karol202.sciorder.client.android.admin.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_products.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import pl.karol202.sciorder.client.android.admin.R
import pl.karol202.sciorder.client.android.admin.ui.adapter.ProductAdapter
import pl.karol202.sciorder.client.android.admin.viewmodel.ProductsViewModel
import pl.karol202.sciorder.client.android.common.components.InflatedFragment
import pl.karol202.sciorder.client.android.common.extensions.*
import pl.karol202.sciorder.common.model.Product

class ProductsFragment : InflatedFragment()
{
	private val productsViewModel by sharedViewModel<ProductsViewModel>()

	private val navController by lazy { findNavController(this) }

	private val adapter = ProductAdapter(productEditListener = { navigateToProductEditFragment(it) },
	                                     productRemoveListener = { showProductRemoveDialog(it) })

	override val layoutRes = R.layout.fragment_products

	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		initRefreshLayout()
		initRecycler()
		initAddButton()

		observeProducts()
		observeLoading()
		observeLoadingError()
		observeUpdateError()
	}

	private fun initRefreshLayout()
	{
		refreshLayoutProducts.setOnRefreshListener { productsViewModel.refreshProducts() }
	}

	private fun initRecycler()
	{
		recyclerProducts.layoutManager = LinearLayoutManager(ctx)
		recyclerProducts.adapter = adapter
		recyclerProducts.addItemDecoration(DividerItemDecoration(ctx, DividerItemDecoration.VERTICAL))
	}

	private fun initAddButton()
	{
		buttonProductAdd.setOnClickListener { navigateToProductEditFragment(null) }
	}

	private fun observeProducts() =
			productsViewModel.productsLiveData.observeNonNull(viewLifecycleOwner) { adapter.products = it }

	private fun observeLoading() =
			productsViewModel.loadingLiveData.observeNonNull(viewLifecycleOwner) { refreshLayoutProducts.isRefreshing = it }

	private fun observeLoadingError() =
			productsViewModel.loadingErrorEventLiveData.observeEvent(viewLifecycleOwner) { showSnackbar(R.string.text_loading_error) }

	private fun observeUpdateError() =
			productsViewModel.updateEventLiveData.observeEvent(viewLifecycleOwner) {
				if(it == ProductsViewModel.UpdateResult.FAILURE) showSnackbar(R.string.text_update_error)
			}

	private fun navigateToProductEditFragment(product: Product?)
	{
		navController.navigate(MainFragmentDirections.actionMainFragmentToProductEditFragment(product?.id))
	}

	private fun showProductRemoveDialog(product: Product)
	{
		ctx.alertDialog {
			setMessage(ctx.getString(R.string.dialog_product_remove, product.name))
			setPositiveButton(R.string.action_remove) { _, _ -> productsViewModel.removeProduct(product) }
			setNegativeButton(R.string.action_cancel, null)
		}.show()
	}
}
