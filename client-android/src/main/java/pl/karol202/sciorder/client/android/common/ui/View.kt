package pl.karol202.sciorder.client.android.common.ui

import android.content.Context
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator

val View.ctx: Context get() = context

fun EditText.setTextIfDiffer(text: String)
{
	if(this.text?.toString() != text) setText(text)
}

val RecyclerView.simpleItemAnimator get() = itemAnimator as? SimpleItemAnimator

fun AutoCompleteTextView.setOnItemSelectedListener(listener: (Any) -> Unit)
{
	onItemSelectedListener = createOnItemSelectedListener(adapter, listener)
}

fun AdapterView<*>.setOnItemSelectedListener(listener: (Any) -> Unit)
{
	onItemSelectedListener = createOnItemSelectedListener(adapter, listener)
}

private fun createOnItemSelectedListener(adapter: Adapter, listener: (Any) -> Unit) =
		object : AdapterView.OnItemSelectedListener {
			override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) =
					listener(adapter.getItem(position))
			override fun onNothingSelected(parent: AdapterView<*>?) { }
		}

fun CompoundButton.setOnCheckedListener(listener: (Boolean) -> Unit) =
		setOnCheckedChangeListener { _, checked -> listener(checked) }
