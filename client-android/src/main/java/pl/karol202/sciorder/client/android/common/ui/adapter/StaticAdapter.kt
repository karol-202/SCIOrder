package pl.karol202.sciorder.client.android.common.ui.adapter

abstract class StaticAdapter<T>(private val items: List<T>) : BasicAdapter<T>()
{
	override fun getItemCount() = items.size

	override fun getItem(position: Int) = items[position]
}
