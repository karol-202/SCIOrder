package pl.karol202.sciorder.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
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

	init
	{
		setTitle(product.name)
		setView(view)
		setButton(AlertDialog.BUTTON_POSITIVE, context.getString(R.string.action_order)) { _, _ -> order() }
		setButton(DialogInterface.BUTTON_NEGATIVE, context.getString(R.string.action_cancel)) { _, _ -> } // To resolve ambiguity
	}

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		recyclerProductOrderParams.layoutManager = LinearLayoutManager(context)
		recyclerProductOrderParams.adapter = ProductParamAdapter(product)
	}

	private fun order()
	{
		orderListener?.onProductOrder(product, 1, mapOf())
	}
}