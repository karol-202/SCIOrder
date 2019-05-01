package pl.karol202.sciorder.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
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
import pl.karol202.sciorder.extensions.alertDialog
import pl.karol202.sciorder.extensions.ctx
import pl.karol202.sciorder.model.OrderedProduct
import pl.karol202.sciorder.model.Product
import pl.karol202.sciorder.viewmodel.OrderViewModel
import pl.karol202.sciorder.viewmodel.ProductViewModel

class ProductsFragment : Fragment(), OnProductOrderListener, OnProductOrderEditListener
{
	private val productViewModel by lazy { ViewModelProviders.of(act).get<ProductViewModel>() }
	private val orderViewModel by lazy { ViewModelProviders.of(act).get<OrderViewModel>() }

	private val productsAdapter = ProductAdapter().apply {
		onProductSelectListener = { product -> showProductOrderDialog(product) }
	}
	private val orderAdapter = OrderAdapter().apply {
		onProductEditListener = { showProductOrderEditDialog(it) }
		onProductRemoveListener = { showProductRemoveDialog(it) }
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
			inflater.inflate(R.layout.fragment_products, container, false)

	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		initRefreshLayout()
		initProductsRecycler()
		initOrderRecycler()
		initOrderButton()

		observeProducts()
		observeLoading()
		observeProductError()

		observeOrder()
		observeOrderError()
	}

	private fun initRefreshLayout()
	{
		refreshLayoutProducts.setOnRefreshListener { productViewModel.refreshProducts() }
	}

	private fun initProductsRecycler()
	{
		recyclerProducts.layoutManager = LinearLayoutManager(context)
		recyclerProducts.adapter = productsAdapter
		recyclerProducts.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
	}

	private fun initOrderRecycler()
	{
		recyclerOrderSheet.layoutManager = LinearLayoutManager(context)
		recyclerOrderSheet.adapter = orderAdapter
		recyclerOrderSheet.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
	}

	private fun initOrderButton()
	{
		buttonOrderSheet.setOnClickListener { if(!orderViewModel.isOrderListEmpty()) orderViewModel.orderAll() }
	}

	private fun observeProducts()
	{
		productViewModel.productsLiveData.observe(viewLifecycleOwner, Observer { productsList ->
			productsList?.let { productsAdapter.products = it }
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

	private fun observeOrder()
	{
		orderViewModel.orderLiveData.observe(viewLifecycleOwner, Observer { products ->
			textOrderSheetProducts.text = resources.getQuantityString(R.plurals.text_order_products, products.size, products.size)
			orderAdapter.orderedProducts = products
			buttonOrderSheet.isEnabled = products.isNotEmpty()
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
		Snackbar.make(view ?: return, message, Snackbar.LENGTH_LONG).apply {
			val layoutParams = view.layoutParams as CoordinatorLayout.LayoutParams
			layoutParams.bottomMargin = resources.getDimensionPixelOffset(R.dimen.snackbar_over_bottom_sheet_margin)
			show()
		}
	}

	private fun showProductOrderDialog(product: Product) =
			fragmentManager?.let { ProductOrderDialogFragment.create(product, this).show(it) }

	private fun showProductOrderEditDialog(orderedProduct: OrderedProduct) =
			fragmentManager?.let { ProductOrderEditDialogFragment.create(orderedProduct, this).show(it) }

	private fun showProductRemoveDialog(product: OrderedProduct)
	{
		ctx.alertDialog {
			setMessage(ctx.getString(R.string.dialog_ordered_product_remove, product.product.name))
			setPositiveButton(R.string.action_remove) { _, _ -> orderViewModel.removeFromOrder(product) }
			setNegativeButton(R.string.action_cancel, null)
		}.show()
	}

	override fun onProductOrder(orderedProduct: OrderedProduct) = orderViewModel.orderSingleProduct(orderedProduct)

	override fun onProductAddToOrder(orderedProduct: OrderedProduct) = orderViewModel.addToOrder(orderedProduct)

	override fun onProductOrderEdit(oldProduct: OrderedProduct, newProduct: OrderedProduct)
	{
		orderViewModel.replaceInOrder(oldProduct, newProduct)
	}
}