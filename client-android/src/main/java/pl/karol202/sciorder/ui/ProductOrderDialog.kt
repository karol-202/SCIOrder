package pl.karol202.sciorder.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.dialog_product_order.*
import pl.karol202.sciorder.R
import pl.karol202.sciorder.components.ExtendedAlertDialog
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
		setButton(DialogInterface.BUTTON_POSITIVE, context.getString(R.string.action_order)) { _, _ -> } // Setting listener through setButton() causes dialog dismiss
		setButton(DialogInterface.BUTTON_NEGATIVE, context.getString(R.string.action_cancel)) { _, _ -> } // Empty listener to resolve ambiguity
		setOnShowListener {
			getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener { tryToOrder() } // Directly setting listener omits dismissal
			window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
			// Workaround for not showing of soft keyboard
		}
	}

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		recyclerProductOrderParams.layoutManager = LinearLayoutManager(context)
		recyclerProductOrderParams.adapter = adapter
	}

	private fun tryToOrder()
	{
		val params = adapter.params.mapKeys { it.key.name }.mapValues { it.value?.toString() ?: return }
		orderListener?.onProductOrder(product, adapter.quantity ?: return, params)
		dismiss()
	}
}