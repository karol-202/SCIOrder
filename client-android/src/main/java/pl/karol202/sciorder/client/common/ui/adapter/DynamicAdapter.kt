package pl.karol202.sciorder.client.common.ui.adapter

import androidx.recyclerview.widget.DiffUtil

abstract class DynamicAdapter<T> : BasicAdapter<T>()
{
	protected var items = emptyList<T>()
		set(value)
		{
			val result = DiffUtil.calculateDiff(StandardDiffCallback(field, value) { getItemId(this) })
			field = value
			result.dispatchUpdatesTo(this)
		}

	override fun getItemCount() = items.size

	override fun getItem(position: Int) = items[position]

	// Returns value unique to each item, needed by diff calculator
	abstract fun getItemId(item: T): Any
}
