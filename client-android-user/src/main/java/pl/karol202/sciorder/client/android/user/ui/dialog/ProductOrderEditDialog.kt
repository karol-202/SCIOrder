package pl.karol202.sciorder.client.android.user.ui.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.dialog_product_order_edit.*
import pl.karol202.sciorder.client.android.user.R
import pl.karol202.sciorder.client.android.user.ui.adapter.ProductParamAdapter
import pl.karol202.sciorder.client.android.user.ui.listener.OnProductOrderEditListener
import pl.karol202.sciorder.client.common.components.ExtendedAlertDialog
import pl.karol202.sciorder.client.common.model.OrderedProduct
import pl.karol202.sciorder.common.Product

class ProductOrderEditDialog(context: Context,
                             private val orderedProduct: OrderedProduct,
                             private val orderListener: OnProductOrderEditListener) : ExtendedAlertDialog(context)
{
	@SuppressLint("InflateParams")
	val view: View = LayoutInflater.from(context).inflate(R.layout.dialog_product_order_edit, null)

	private val adapter = ProductParamAdapter(context, orderedProduct.getFakeProduct(), orderedProduct.quantity)

	init
	{
		setTitle(orderedProduct.product.name)
		setView(view)
		setOnShowListener {
			// Workaround for not showing of soft keyboard
			window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
		}
	}

	// Returns product with currently set parameter values as its default values
	private fun OrderedProduct.getFakeProduct(): Product
	{
		fun Product.Parameter.toFakeParam() = copy(attributes = attributes.copy(defaultValue = parameters[name]))

		val productParams = product.parameters.map { it.toFakeParam() }
		return product.copy(parameters = productParams)
	}

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		recyclerProductOrderEditParams.layoutManager = LinearLayoutManager(context)
		recyclerProductOrderEditParams.adapter = adapter

		buttonProductOrderEdit.setOnClickListener { tryToApply() }
	}

	private fun tryToApply()
	{
		orderListener.onProductOrderEdit(orderedProduct, getValidProductOrder() ?: return)
		dismiss()
	}

	private fun getValidProductOrder(): OrderedProduct?
	{
		val params = adapter.params.mapKeys { it.key.name }.mapValues { it.value?.toString() ?: return null }
		return OrderedProduct(orderedProduct.id, orderedProduct.product, adapter.quantity ?: return null, params)
	}
}
