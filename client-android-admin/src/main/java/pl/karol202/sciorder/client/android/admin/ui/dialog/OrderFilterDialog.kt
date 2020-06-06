package pl.karol202.sciorder.client.android.admin.ui.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.dialog_order_filter.*
import pl.karol202.sciorder.client.android.admin.R
import pl.karol202.sciorder.client.android.admin.ui.adapter.OrderFilterAdapter
import pl.karol202.sciorder.common.model.Order

class OrderFilterDialog(context: Context,
                        filter: Set<Order.Status>,
                        private val filterSetListener: (Set<Order.Status>) -> Unit) : AlertDialog(context)
{
	@SuppressLint("InflateParams")
	private val view = LayoutInflater.from(context).inflate(R.layout.dialog_order_filter, null)

	private val adapter = OrderFilterAdapter(filter)

	init
	{
		setTitle(R.string.dialog_order_filter)
		setView(view)
		setButton(BUTTON_POSITIVE, context.getString(R.string.action_filter_edit)) { _, _ -> apply() }
	}

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		recyclerOrderFilter.layoutManager = LinearLayoutManager(context)
		recyclerOrderFilter.adapter = adapter
	}

	private fun apply() = filterSetListener(adapter.filter)
}
