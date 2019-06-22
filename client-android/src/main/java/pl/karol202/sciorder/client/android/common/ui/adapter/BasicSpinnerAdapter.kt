package pl.karol202.sciorder.client.android.common.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.extensions.LayoutContainer
import pl.karol202.sciorder.client.android.common.ui.ctx

abstract class BasicSpinnerAdapter<T> : BaseAdapter()
{
	abstract class ViewHolder<T>(final override val containerView: View) : LayoutContainer
	{
		protected val ctx = containerView.ctx

		open fun bind(item: T) { }
	}

	abstract val layout: Int

	abstract fun createViewHolder(view: View): ViewHolder<T>

	abstract override fun getItem(position: Int): T

	override fun getItemId(position: Int) = position.toLong()

	override fun getView(position: Int, convertView: View?, parent: ViewGroup): View
	{
		@Suppress("UNCHECKED_CAST")
		val viewHolder = convertView?.tag as? ViewHolder<T> ?: createViewHolder(parent)
		viewHolder.bind(getItem(position))
		return viewHolder.containerView
	}

	private fun createViewHolder(parent: ViewGroup): ViewHolder<T>
	{
		val view = LayoutInflater.from(parent.ctx).inflate(layout, parent, false)
		return createViewHolder(view).also { view.tag = it }
	}
}
