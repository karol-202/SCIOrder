package pl.karol202.sciorder.client.android.admin.ui.adapter

import android.content.Context
import android.view.View
import kotlinx.android.synthetic.main.item_product_param_attr_enum_value.*
import pl.karol202.sciorder.client.android.admin.R
import pl.karol202.sciorder.client.android.admin.ui.dialog.ProductParamAttrEnumNewValueDialog
import pl.karol202.sciorder.client.android.common.ui.adapter.BasicAdapter
import pl.karol202.sciorder.client.android.common.ui.adapter.DynamicAdapter

class ProductParamAttrEnumValuesAdapter(private val ctx: Context,
                                        private val onValuesUpdate: (values: List<String>, default: String?) -> Unit) :
		DynamicAdapter<ProductParamAttrEnumValuesAdapter.EnumEntry?>()
{
	data class EnumEntry(val id: Long,
	                     val value: String)

	inner class ValueViewHolder(private val view: View) : BasicAdapter.ViewHolder<EnumEntry?>(view)
	{
		override fun bind(item: EnumEntry?)
		{
			item ?: throw IllegalArgumentException()

			radioProductEditParamAttrEnumValue.setOnCheckedChangeListener { _, checked -> onSelectionChange(item, checked) }
			radioProductEditParamAttrEnumValue.isChecked = selection == item.value

			textProductEditParamAttrEnumValue.text = item.value

			buttonProductEditParamAttrEnumValueRemove.setOnClickListener { removeValue(item) }
		}

		private fun onSelectionChange(item: EnumEntry, selected: Boolean)
		{
			selection = item.value.takeIf { selected }
		}
	}

	inner class NullViewHolder(private val view: View) : BasicAdapter.ViewHolder<EnumEntry?>(view)
	{
		override fun bind(item: EnumEntry?) = view.setOnClickListener { showNewValueDialog() }
	}

	companion object
	{
		private const val TYPE_VALUE = 0
		private const val TYPE_NULL = 1
		
		private const val NULL_ITEM_ID = -1
	}

	var values: List<String>
		get() = items.mapNotNull { it?.value }
		set(value) { items = value.map { it.asEntry() } + null }

	var selection: String? = null
		set(value)
		{
			if(field == value) return
			field = value
			updateViewIf { it.value == field || it.value == value }
		}
	
	override fun getLayout(viewType: Int) = when(viewType)
	{
		TYPE_VALUE -> R.layout.item_product_param_attr_enum_value
		TYPE_NULL -> R.layout.item_product_param_attr_enum_value_null
		else -> throw IllegalArgumentException()
	}

	override fun createViewHolder(view: View, viewType: Int) = when(viewType)
	{
		TYPE_VALUE -> ValueViewHolder(view)
		TYPE_NULL -> NullViewHolder(view)
		else -> throw IllegalArgumentException()
	}

	override fun getItemId(item: EnumEntry?) = item?.id ?: NULL_ITEM_ID

	override fun getItemViewType(position: Int) = if(getItem(position) != null) TYPE_VALUE else TYPE_NULL

	private fun showNewValueDialog() = ProductParamAttrEnumNewValueDialog(ctx) { addNewValue(it.asEntry()) }.show()

	private fun addNewValue(item: EnumEntry)
	{
		items = items.dropLast(1) + item + null
		onValuesUpdate()
	}

	private fun removeValue(item: EnumEntry)
	{
		items = items - item
		onValuesUpdate()
	}

	private fun onValuesUpdate() = onValuesUpdate(values, selection)
	
	private fun updateViewIf(predicate: (EnumEntry) -> Boolean) =
			items.withIndex()
					.filter { (_, entry) -> entry != null && predicate(entry) }
					.forEach { (index, _) -> notifyItemChanged(index) }
	
	// Replace nanoTime() with some better id generator ?
	private fun String.asEntry() = EnumEntry(System.nanoTime(), this)
}
