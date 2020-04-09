package pl.karol202.sciorder.client.android.common.ui.adapter

import androidx.recyclerview.widget.DiffUtil

abstract class DynamicAdapter<T> : BasicAdapter<T>()
{
	protected open var items = emptyList<T>()
		set(value)
		{
			val result = DiffUtil.calculateDiff(StandardDiffCallback(field, value) { getItemId(this) })
			field = value
			result.dispatchUpdatesTo(this)
		}

	override fun getItemCount() = items.size

	override fun getItem(position: Int) = items[position]
	
	override fun getItemId(position: Int) = getItemId(getItem(position))
	
	abstract fun getItemId(item: T): Long
}
