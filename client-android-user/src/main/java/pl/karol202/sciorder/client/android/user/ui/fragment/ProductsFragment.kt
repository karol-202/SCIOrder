package pl.karol202.sciorder.client.android.user.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_products.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import pl.karol202.sciorder.client.android.common.component.InflatedFragment
import pl.karol202.sciorder.client.android.common.extension.*
import pl.karol202.sciorder.client.android.user.R
import pl.karol202.sciorder.client.android.user.ui.adapter.OrderedProductAdapter
import pl.karol202.sciorder.client.android.user.ui.adapter.ProductAdapter
import pl.karol202.sciorder.client.android.user.ui.dialog.fragment.OrderDialogFragment
import pl.karol202.sciorder.client.android.user.ui.dialog.fragment.ProductOrderDialogFragment
import pl.karol202.sciorder.client.android.user.ui.dialog.fragment.ProductOrderEditDialogFragment
import pl.karol202.sciorder.client.android.user.ui.listener.OnOrderDetailsSetListener
import pl.karol202.sciorder.client.android.user.ui.listener.OnProductOrderEditListener
import pl.karol202.sciorder.client.android.user.ui.listener.OnProductOrderListener
import pl.karol202.sciorder.client.android.user.viewmodel.OrderComposeViewModel
import pl.karol202.sciorder.client.android.user.viewmodel.ProductsViewModel
import pl.karol202.sciorder.client.common.model.OrderedProduct
import pl.karol202.sciorder.common.Order
import pl.karol202.sciorder.common.Product

class ProductsFragment : InflatedFragment(), OnProductOrderListener, OnProductOrderEditListener, OnOrderDetailsSetListener
{
	private val productsViewModel by sharedViewModel<ProductsViewModel>()
	private val ordersViewModel by sharedViewModel<OrderComposeViewModel>()

	private val productsAdapter = ProductAdapter().apply {
		onProductSelectListener = { product -> showProductOrderDialog(product) }
	}
	private val orderAdapter = OrderedProductAdapter().apply {
		onProductEditListener = { showProductOrderEditDialog(it) }
		onProductRemoveListener = { showProductRemoveDialog(it) }
	}

	override val layoutRes = R.layout.fragment_products

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
		refreshLayoutProducts.setOnRefreshListener { productsViewModel.refreshProducts() }
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
		buttonOrderSheet.setOnClickListener {
			showAllProductsOrderDialog(ordersViewModel.getProductsInOrderOrNull() ?: return@setOnClickListener)
		}
	}

	private fun observeProducts() =
			productsViewModel.productsLiveData.observeNonNull(viewLifecycleOwner) { productsAdapter.products = it }

	private fun observeLoading() =
			productsViewModel.loadingLiveData.observeNonNull(viewLifecycleOwner) { if(!it) refreshLayoutProducts.isRefreshing = false }

	private fun observeProductError() =
			productsViewModel.loadingErrorEventLiveData.observeEvent(viewLifecycleOwner) { showSnackbar(R.string.text_loading_error) }

	private fun observeOrder() =
			ordersViewModel.orderLiveData.observeNonNull(viewLifecycleOwner) { products ->
				textOrderSheetProducts.text = resources.getQuantityString(R.plurals.text_products, products.size, products.size)
				orderAdapter.orderedProducts = products
				buttonOrderSheet.isEnabled = products.isNotEmpty()
			}

	private fun observeOrderError() =
			ordersViewModel.errorEventLiveData.observeEvent(viewLifecycleOwner) {
				if(it == OrderComposeViewModel.OrderResult.SUCCESS) showSnackbar(R.string.text_order_success)
				else showSnackbar(R.string.text_order_error)
			}

	private fun showSnackbar(@StringRes message: Int)
	{
		showSnackbar(message) {
			val layoutParams = view.layoutParams as CoordinatorLayout.LayoutParams
			layoutParams.bottomMargin = resources.getDimensionPixelOffset(R.dimen.snackbar_over_bottom_sheet_margin)
		}
	}

	private fun showProductOrderDialog(product: Product) =
			ProductOrderDialogFragment.create(product, this).show(fragmentManager)

	private fun showProductOrderEditDialog(orderedProduct: OrderedProduct) =
			ProductOrderEditDialogFragment.create(orderedProduct, this).show(fragmentManager)

	private fun showProductRemoveDialog(product: OrderedProduct)
	{
		ctx.alertDialog {
			setMessage(ctx.getString(R.string.dialog_ordered_product_remove, product.product.name))
			setPositiveButton(R.string.action_remove) { _, _ -> ordersViewModel.removeFromOrder(product) }
			setNegativeButton(R.string.action_cancel, null)
		}.show()
	}

	private fun showAllProductsOrderDialog(orderedProducts: List<OrderedProduct>) =
			showOrderDialog(OnOrderDetailsSetListener.Case.OrderAll(orderedProducts))

	private fun showSingleProductOrderDialog(orderedProduct: OrderedProduct) =
			showOrderDialog(OnOrderDetailsSetListener.Case.OrderSingle(orderedProduct))

	private fun showOrderDialog(case: OnOrderDetailsSetListener.Case) =
			OrderDialogFragment.create(case, this).show(fragmentManager)

	override fun onProductOrder(orderedProduct: OrderedProduct) = showSingleProductOrderDialog(orderedProduct)

	override fun onProductAddToOrder(orderedProduct: OrderedProduct) = ordersViewModel.addToOrder(orderedProduct)

	override fun onProductOrderEdit(oldProduct: OrderedProduct, newProduct: OrderedProduct) =
			ordersViewModel.replaceInOrder(oldProduct, newProduct)

	override fun onOrderDetailsSet(case: OnOrderDetailsSetListener.Case, details: Order.Details) = when(case)
	{
		is OnOrderDetailsSetListener.Case.OrderSingle -> ordersViewModel.orderSingleProduct(case.orderedProduct, details)
		is OnOrderDetailsSetListener.Case.OrderAll -> ordersViewModel.orderAll(details)
	}
}
