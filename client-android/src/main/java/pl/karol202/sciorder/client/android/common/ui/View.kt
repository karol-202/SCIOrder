package pl.karol202.sciorder.client.android.common.ui

import android.content.Context
import android.view.View
import android.widget.AdapterView

val View.ctx: Context get() = context

fun AdapterView<*>.setOnItemSelectedListener(listener: (Any) -> Unit)
{
	onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
		override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) =
				listener(adapter.getItem(position))

		override fun onNothingSelected(parent: AdapterView<*>?) { }
	}
}
