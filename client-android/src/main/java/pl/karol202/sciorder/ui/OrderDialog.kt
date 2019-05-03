package pl.karol202.sciorder.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.dialog_order.*
import pl.karol202.sciorder.R
import pl.karol202.sciorder.components.ExtendedAlertDialog
import pl.karol202.sciorder.model.Order

class OrderDialog(context: Context,
                  private val case: OnOrderDetailsSetListener.Case,
                  private val orderListener: OnOrderDetailsSetListener) : ExtendedAlertDialog(context)
{
	@SuppressLint("InflateParams")
	val view: View = LayoutInflater.from(context).inflate(R.layout.dialog_order, null)

	init
	{
		setTitle(R.string.dialog_order)
		setView(view)
	}

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		recyclerOrder.layoutManager = LinearLayoutManager(context)
		recyclerOrder.adapter = SimpleOrderAdapter(case.orderedProducts)
		recyclerOrder.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

		buttonOrderOrder.setOnClickListener { order() }
		buttonOrderCancel.setOnClickListener { dismiss() }
	}

	private fun order()
	{
		val location = editTextOrderLocation.text?.toString() ?: ""
		val recipient = editTextOrderRecipient.text?.toString() ?: ""
		val details = Order.Details(location, recipient)
		orderListener.onOrderDetailsSet(case, details)
		dismiss()
	}
}