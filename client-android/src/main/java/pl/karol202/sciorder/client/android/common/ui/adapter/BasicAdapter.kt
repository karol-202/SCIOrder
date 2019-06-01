package pl.karol202.sciorder.client.android.common.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import pl.karol202.sciorder.client.android.common.extensions.ctx

abstract class BasicAdapter<T> : RecyclerView.Adapter<BasicAdapter.ViewHolder<T>>()
{
	abstract class ViewHolder<T>(final override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer
	{
		val ctx = containerView.ctx

		open fun bind(item: T) { }
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<T>
	{
		val view = LayoutInflater.from(parent.ctx).inflate(getLayout(viewType), parent, false)
		return createViewHolder(view, viewType)
	}

	@LayoutRes
	abstract fun getLayout(viewType: Int): Int

	abstract fun createViewHolder(view: View, viewType: Int): ViewHolder<T>

	override fun onBindViewHolder(holder: ViewHolder<T>, position: Int)
	{
		holder.bind(getItem(position))
	}

	abstract fun getItem(position: Int): T
}
