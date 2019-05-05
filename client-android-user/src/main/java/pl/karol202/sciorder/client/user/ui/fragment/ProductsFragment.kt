package pl.karol202.sciorder.client.user.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_products.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import pl.karol202.sciorder.client.common.extensions.*
import pl.karol202.sciorder.client.user.R
import pl.karol202.sciorder.client.common.model.OrderedProduct
import pl.karol202.sciorder.client.user.ui.adapter.OrderedProductAdapter
import pl.karol202.sciorder.client.user.ui.adapter.ProductAdapter
import pl.karol202.sciorder.client.user.ui.dialog.fragment.OrderDialogFragment
import pl.karol202.sciorder.client.user.ui.dialog.fragment.ProductOrderDialogFragment
import pl.karol202.sciorder.client.user.ui.dialog.fragment.ProductOrderEditDialogFragment
import pl.karol202.sciorder.client.user.ui.listener.OnOrderDetailsSetListener
import pl.karol202.sciorder.client.user.ui.listener.OnProductOrderEditListener
import pl.karol202.sciorder.client.user.ui.listener.OnProductOrderListener
import pl.karol202.sciorder.client.user.viewmodel.OrderViewModel
import pl.karol202.sciorder.client.user.viewmodel.ProductViewModel
import pl.karol202.sciorder.common.model.Order
import pl.karol202.sciorder.common.model.Product

class ProductsFragment : Fragment(), OnProductOrderListener, OnProductOrderEditListener, OnOrderDetailsSetListener
{
	private val productViewModel by sharedViewModel<ProductViewModel>()
	private val orderViewModel by sharedViewModel<OrderViewModel>()

	private val productsAdapter = ProductAdapter().apply {
		onProductSelectListener = { product -> showProductOrderDialog(product) }
	}
	private val orderAdapter = OrderedProductAdapter().apply {
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
		buttonOrderSheet.setOnClickListener {
			showAllProductsOrderDialog(orderViewModel.getProductsInOrder() ?: return@setOnClickListener)
		}
	}

	private fun observeProducts() =
			productViewModel.productsLiveData.observeNonNull(viewLifecycleOwner) { productsAdapter.items = it }

	private fun observeLoading() =
			productViewModel.loadingLiveData.observeNonNull(viewLifecycleOwner) { refreshLayoutProducts.isRefreshing = it }

	private fun observeProductError() =
			productViewModel.errorEventLiveData.observeEvent(viewLifecycleOwner) { showErrorSnackbar(R.string.text_loading_error) }

	private fun observeOrder() =
			orderViewModel.orderLiveData.observeNonNull(viewLifecycleOwner) { products ->
				textOrderSheetProducts.text = resources.getQuantityString(R.plurals.text_products, products.size, products.size)
				orderAdapter.items = products
				buttonOrderSheet.isEnabled = products.isNotEmpty()
			}

	private fun observeOrderError() =
			orderViewModel.errorEventLiveData.observeEvent(viewLifecycleOwner) { showErrorSnackbar(R.string.text_order_error) }

	private fun showErrorSnackbar(@StringRes message: Int)
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
			setPositiveButton(R.string.action_remove) { _, _ -> orderViewModel.removeFromOrder(product) }
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

	override fun onProductAddToOrder(orderedProduct: OrderedProduct) = orderViewModel.addToOrder(orderedProduct)

	override fun onProductOrderEdit(oldProduct: OrderedProduct, newProduct: OrderedProduct) =
			orderViewModel.replaceInOrder(oldProduct, newProduct)

	override fun onOrderDetailsSet(case: OnOrderDetailsSetListener.Case, details: Order.Details) = when(case)
	{
		is OnOrderDetailsSetListener.Case.OrderSingle -> orderViewModel.orderSingleProduct(case.orderedProduct, details)
		is OnOrderDetailsSetListener.Case.OrderAll -> orderViewModel.orderAll(details)
	}
}
