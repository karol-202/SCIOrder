package pl.karol202.sciorder.client.android.admin.ui.adapter

import android.content.Context
import android.view.View
import kotlinx.android.synthetic.main.item_product_param_attr_enum_value.*
import pl.karol202.sciorder.client.android.admin.R
import pl.karol202.sciorder.client.android.admin.ui.dialog.ProductParamAttrEnumNewValueDialog
import pl.karol202.sciorder.client.android.common.ui.adapter.BasicAdapter
import pl.karol202.sciorder.client.android.common.ui.adapter.DynamicAdapter
import pl.karol202.sciorder.client.android.common.util.randomUUIDString

class ProductParamAttrEnumValuesAdapter(private val ctx: Context,
                                        private val onValuesUpdate: (values: List<String>, default: String?) -> Unit) :
		DynamicAdapter<ProductParamAttrEnumValuesAdapter.EnumEntry?>()
{
	data class EnumEntry(val id: String,
	                     val value: String,
	                     val selected: Boolean)
	{
		companion object
		{
			val NULL: EnumEntry? = null
		}
	}

	inner class ValueViewHolder(view: View) : BasicAdapter.ViewHolder<EnumEntry?>(view)
	{
		override fun bind(item: EnumEntry?)
		{
			if(item == null) throw IllegalArgumentException()

			radioProductEditParamAttrEnumValue.setOnCheckedChangeListener { _, checked -> onSelectionChange(item, checked) }
			radioProductEditParamAttrEnumValue.isChecked = _selection == item

			textProductEditParamAttrEnumValue.text = item.value

			buttonProductEditParamAttrEnumValueRemove.setOnClickListener { removeValue(item) }
		}

		private fun onSelectionChange(item: EnumEntry, selected: Boolean)
		{
			if(selected != item.selected) _selection = item.takeIf { selected }
		}
	}

	inner class NullViewHolder(private val view: View) : BasicAdapter.ViewHolder<EnumEntry?>(view)
	{
		override fun bind(item: EnumEntry?)
		{
			super.bind(item)
			view.setOnClickListener { addNewValue() }
		}
	}

	companion object
	{
		private const val TYPE_VALUE = 0
		private const val TYPE_NULL = 1
	}

	var values: List<String>
		get() = items.mapNotNull { it?.value }
		set(value) { items = value.map { it.withId() } + EnumEntry.NULL }

	private var _selection: EnumEntry?
		get() = items.find { it?.selected ?: false }
		set(value)
		{
			items = items.map { it?.copy(selected = it == value) }
			onValuesUpdate()
		}
	var selection: String?
		get() = _selection?.value
		set(value) { _selection = items.find { it?.value == value } }

	private fun String.withId(id: String = randomUUIDString()) = EnumEntry(id, this, false)

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

	override fun getItemId(item: EnumEntry?) = item?.id ?: ""

	override fun getItemViewType(position: Int) = if(getItem(position) != null) TYPE_VALUE else TYPE_NULL

	private fun addNewValue() = ProductParamAttrEnumNewValueDialog(ctx) { addNewValue(it.withId()) }.show()

	private fun addNewValue(item: EnumEntry)
	{
		items = items.dropLast(1) + item + EnumEntry.NULL
		onValuesUpdate()
	}

	private fun removeValue(item: EnumEntry)
	{
		items = items - item
		onValuesUpdate()
	}

	private fun onValuesUpdate() = onValuesUpdate(values, selection)
}
