package pl.karol202.sciorder.client.common.extensions

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.TextView

val View.ctx: Context get() = context

fun TextView.addAfterTextChangedListener(listener: (String?) -> Unit) = addTextChangedListener(object : TextWatcher {
	override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

	override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }

	override fun afterTextChanged(s: Editable?) = listener(s?.toString())
})

fun AdapterView<*>.setOnItemSelectedListener(listener: (Any) -> Unit)
{
	onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
		override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
		{
			listener(adapter.getItem(position))
		}

		override fun onNothingSelected(parent: AdapterView<*>?) { }
	}
}
