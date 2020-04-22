package pl.karol202.sciorder.client.android.common.ui

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.CompoundButton
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator

val View.ctx: Context get() = context

fun EditText.setTextIfDiffer(text: String)
{
	if(this.text?.toString() != text) setText(text)
}

val RecyclerView.simpleItemAnimator get() = itemAnimator as? SimpleItemAnimator

fun AdapterView<*>.setOnItemSelectedListener(listener: (Any) -> Unit)
{
	onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
		override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) =
				listener(adapter.getItem(position))

		override fun onNothingSelected(parent: AdapterView<*>?) { }
	}
}

fun CompoundButton.setOnCheckedListener(listener: (Boolean) -> Unit) =
		setOnCheckedChangeListener { _, checked -> listener(checked) }
