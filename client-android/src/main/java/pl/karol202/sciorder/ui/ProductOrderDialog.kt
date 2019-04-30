package pl.karol202.sciorder.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.dialog_product_order.*
import pl.karol202.sciorder.R
import pl.karol202.sciorder.components.ExtendedAlertDialog
import pl.karol202.sciorder.model.OrderedProduct
import pl.karol202.sciorder.model.Product

class ProductOrderDialog(context: Context,
                         private val product: Product,
                         private val orderListener: OnProductOrderListener?) : ExtendedAlertDialog(context)
{
	@SuppressLint("InflateParams")
	val view: View = LayoutInflater.from(context).inflate(R.layout.dialog_product_order, null)

	private val adapter = ProductParamAdapter(context, product)

	init
	{
		setTitle(product.name)
		setView(view)
		setOnShowListener {
			// Workaround for not showing of soft keyboard
			window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
		}
	}

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		recyclerProductOrderParams.layoutManager = LinearLayoutManager(context)
		recyclerProductOrderParams.adapter = adapter

		buttonProductOrder.setOnClickListener { tryToOrder() }
		buttonProductAddToOrder.setOnClickListener { tryToAddToOrder() }
	}

	private fun tryToOrder()
	{
		orderListener?.onProductOrder(getValidProductOrder() ?: return)
		dismiss()
	}

	private fun tryToAddToOrder()
	{
		orderListener?.onProductAddToOrder(getValidProductOrder() ?: return)
		dismiss()
	}

	private fun getValidProductOrder(): OrderedProduct?
	{
		val params = adapter.params.mapKeys { it.key.name }.mapValues { it.value?.toString() ?: return null }
		return OrderedProduct(product, adapter.quantity ?: return null, params)
	}
}