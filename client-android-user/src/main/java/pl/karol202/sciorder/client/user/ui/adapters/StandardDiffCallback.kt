package pl.karol202.sciorder.client.user.ui.adapters

import androidx.recyclerview.widget.DiffUtil

class StandardDiffCallback<T>(private val oldList: List<T>,
                              private val newList: List<T>,
                              private val idSelector: T.() -> Any) : DiffUtil.Callback()
{
	override fun getOldListSize() = oldList.size

	override fun getNewListSize() = newList.size

	override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
		oldList[oldItemPosition].idSelector() == newList[newItemPosition].idSelector()

	override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
		oldList[oldItemPosition] == newList[newItemPosition]
}
