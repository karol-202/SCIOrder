package pl.karol202.sciorder.client.android.admin.ui.adapter

import android.text.InputType
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.item_product_param_attrs_bool.*
import kotlinx.android.synthetic.main.item_product_param_attrs_enum.*
import kotlinx.android.synthetic.main.item_product_param_attrs_numeric.*
import kotlinx.android.synthetic.main.item_product_param_attrs_text.*
import pl.karol202.sciorder.client.android.admin.R
import pl.karol202.sciorder.client.android.admin.ui.DividerItemDecorationWithoutLast
import pl.karol202.sciorder.client.android.common.ui.adapter.DynamicAdapter
import pl.karol202.sciorder.client.android.common.ui.ctx
import pl.karol202.sciorder.client.android.common.ui.setOnCheckedListener
import pl.karol202.sciorder.client.android.common.ui.setTextIfDiffer
import pl.karol202.sciorder.common.model.ProductParameter
import pl.karol202.sciorder.common.util.isValidInt
import pl.karol202.sciorder.common.validation.MAX_VALUE_LENGTH
import pl.karol202.sciorder.common.validation.isDefaultValueValidFor
import pl.karol202.sciorder.common.validation.isMaximalValueValidFor
import pl.karol202.sciorder.common.validation.isMinimalValueValidFor

private typealias AttrsUpdateListener = (ProductParameter.Attributes) -> Unit

private fun Number.toStringWithNoZeros() = if(toFloat().isValidInt()) toInt().toString() else toFloat().toString()

private fun String.toFloatIfValidOrNull() = when
{
	endsWith('.') || endsWith(',') -> null
	else -> toFloatOrNull()
}

class ProductParamAttrAdapter(private val onAttrsUpdate: AttrsUpdateListener) : DynamicAdapter<ProductParameter>()
{
	class ViewHolderText(view: View,
	                     private val onAttrsUpdate: AttrsUpdateListener) : ViewHolder<ProductParameter>(view)
	{
		private var attributes: ProductParameter.Attributes? = null
		
		init
		{
			editLayoutProductEditParamAttrsTextDefault.counterMaxLength = ProductParameter.MAX_VALUE_LENGTH
			editTextProductEditParamAttrsTextDefault.doAfterTextChanged { updateDefaultValue(it.toString()) }
		}
		
		override fun bind(item: ProductParameter)
		{
			this.attributes = item.attributes
			
			editTextProductEditParamAttrsTextDefault.setTextIfDiffer(item.attributes.defaultValue.orEmpty())
			
			validate(item)
		}
		
		private fun validate(parameter: ProductParameter)
		{
			editLayoutProductEditParamAttrsTextDefault.error =
					if(parameter.attributes.isDefaultValueValidFor(parameter.type)) null
					else ctx.getString(R.string.text_product_edit_param_error_default_value)
		}
		
		private fun updateDefaultValue(value: String)
		{
			val attributes = attributes ?: return
			onAttrsUpdate(attributes.copy(defaultValue = value))
		}
	}
	
	class ViewHolderNumeric(view: View,
	                        private val onAttrsUpdate: AttrsUpdateListener) : ViewHolder<ProductParameter>(view)
	{
		private sealed class NumberType(val inputType: Int,
		                                val parse: (String) -> Number?,
		                                val toText: (Number?) -> String)
		{
			object Integer : NumberType(
					inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED,
					parse = { it.toIntOrNull() },
					toText = { it?.toInt()?.toString().orEmpty() })
			
			object Decimal : NumberType(
					inputType = Integer.inputType or InputType.TYPE_NUMBER_FLAG_DECIMAL,
					parse = { it.toFloatIfValidOrNull() },
					toText = { it?.toFloat()?.toStringWithNoZeros().orEmpty() })
		}
		
		private data class BoundData(val attributes: ProductParameter.Attributes,
		                             val numberType: NumberType)
		
		private var data: BoundData? = null
		
		init
		{
			editTextProductEditParamAttrsNumericDefault.doAfterTextChanged { updateDefaultValue(it?.toString()) }
			editTextProductEditParamAttrsNumericMin.doAfterTextChanged { updateMinValue(it?.toString()) }
			editTextProductEditParamAttrsNumericMax.doAfterTextChanged { updateMaxValue(it?.toString()) }
		}
		
		override fun bind(item: ProductParameter)
		{
			val numberType = when(item.type)
			{
				ProductParameter.Type.INT -> NumberType.Integer
				ProductParameter.Type.FLOAT -> NumberType.Decimal
				else -> throw IllegalAccessException()
			}
			data = BoundData(item.attributes, numberType)
			
			editTextProductEditParamAttrsNumericDefault.inputType = numberType.inputType
			editTextProductEditParamAttrsNumericMin.inputType = numberType.inputType
			editTextProductEditParamAttrsNumericMax.inputType = numberType.inputType
			
			editTextProductEditParamAttrsNumericDefault.setTextIfDiffer(item.attributes.defaultValue.orEmpty())
			editTextProductEditParamAttrsNumericMin.setTextIfDiffer(item.attributes.minimalValue.let(numberType.toText))
			editTextProductEditParamAttrsNumericMax.setTextIfDiffer(item.attributes.maximalValue.let(numberType.toText))
			
			validate(item)
		}
		
		private fun validate(parameter: ProductParameter)
		{
			validateDefaultValue(parameter)
			validateMinimalValue(parameter)
			validateMaximalValue(parameter)
		}
		
		private fun validateDefaultValue(parameter: ProductParameter)
		{
			editLayoutProductEditParamAttrsNumericDefault.error =
					if(parameter.attributes.isDefaultValueValidFor(parameter.type)) null
					else ctx.getString(R.string.text_product_edit_param_error_default_value)
		}
		
		private fun validateMinimalValue(parameter: ProductParameter)
		{
			editLayoutProductEditParamAttrsNumericMin.error =
					if(parameter.attributes.isMinimalValueValidFor(parameter.type)) null
					else ctx.getString(R.string.text_product_edit_param_error_min_value)
		}
		
		private fun validateMaximalValue(parameter: ProductParameter)
		{
			editLayoutProductEditParamAttrsNumericMax.error =
					if(parameter.attributes.isMaximalValueValidFor(parameter.type)) null
					else ctx.getString(R.string.text_product_edit_param_error_max_value)
		}
		
		private fun updateDefaultValue(text: String?) = updateValue(text) { copy(defaultValue = it?.toStringWithNoZeros()) }
		
		private fun updateMinValue(text: String?) = updateValue(text) { copy(minimalValue = it?.toFloat()) }
		
		private fun updateMaxValue(text: String?) = updateValue(text) { copy(maximalValue = it?.toFloat()) }
		
		private fun updateValue(text: String?,
		                        attrsBuilder: ProductParameter.Attributes.(Number?) -> ProductParameter.Attributes)
		{
			val data = data ?: return
			val value = if(text.isNullOrBlank()) null else data.numberType.parse(text) ?: return
			onAttrsUpdate(data.attributes.attrsBuilder(value))
		}
	}
	
	class ViewHolderBool(view: View,
	                     private val onAttrsUpdate: AttrsUpdateListener) : ViewHolder<ProductParameter>(view)
	{
		override fun bind(item: ProductParameter)
		{
			checkProductEditParamAttrsBoolDefault.isChecked = item.attributes.defaultValue.orEmpty().toBoolean()
			checkProductEditParamAttrsBoolDefault.setOnCheckedListener { updateDefaultValue(item.attributes, it) }
		}
		
		private fun updateDefaultValue(attributes: ProductParameter.Attributes, value: Boolean) =
				onAttrsUpdate(attributes.copy(defaultValue = value.toString()))
	}
	
	class ViewHolderEnum(view: View,
	                     private val onAttrsUpdate: AttrsUpdateListener) : ViewHolder<ProductParameter>(view)
	{
		private val adapter = ProductParamAttrEnumValuesAdapter(view.ctx) { values, default -> updateValues(values, default) }
		
		private var attributes: ProductParameter.Attributes? = null
		
		init
		{
			recyclerProductEditParamAttrsEnum.layoutManager = LinearLayoutManager(ctx)
			recyclerProductEditParamAttrsEnum.adapter = adapter
			recyclerProductEditParamAttrsEnum.addItemDecoration(DividerItemDecorationWithoutLast(ctx))
		}
		
		override fun bind(item: ProductParameter)
		{
			this.attributes = item.attributes
			
			adapter.values = item.attributes.enumValues ?: emptyList()
			adapter.selection = item.attributes.defaultValue
		}
		
		private fun updateValues(enumValues: List<String>, defaultValue: String?)
		{
			val attributes = attributes ?: return
			onAttrsUpdate(attributes.copy(enumValues = enumValues, defaultValue = defaultValue))
		}
	}
	
	companion object
	{
		private const val VIEW_TYPE_TEXT = 0
		private const val VIEW_TYPE_NUMERIC = 1
		private const val VIEW_TYPE_BOOL = 2
		private const val VIEW_TYPE_ENUM = 3
		
		private const val ITEM_ID = 0L
	}
	
	var parameter: ProductParameter?
		get() = items.singleOrNull()
		set(value) { items = listOfNotNull(value) }

	override fun getLayout(viewType: Int) = when(viewType)
	{
		VIEW_TYPE_TEXT -> R.layout.item_product_param_attrs_text
		VIEW_TYPE_NUMERIC -> R.layout.item_product_param_attrs_numeric
		VIEW_TYPE_BOOL -> R.layout.item_product_param_attrs_bool
		VIEW_TYPE_ENUM -> R.layout.item_product_param_attrs_enum
		else -> throw IllegalArgumentException()
	}

	override fun createViewHolder(view: View, viewType: Int) = when(viewType)
	{
		VIEW_TYPE_TEXT -> ViewHolderText(view, onAttrsUpdate)
		VIEW_TYPE_NUMERIC -> ViewHolderNumeric(view, onAttrsUpdate)
		VIEW_TYPE_BOOL -> ViewHolderBool(view, onAttrsUpdate)
		VIEW_TYPE_ENUM -> ViewHolderEnum(view, onAttrsUpdate)
		else -> throw IllegalArgumentException()
	}

	override fun getItemViewType(position: Int) = when(getItem(position).type)
	{
		ProductParameter.Type.TEXT -> VIEW_TYPE_TEXT
		ProductParameter.Type.INT,
		ProductParameter.Type.FLOAT -> VIEW_TYPE_NUMERIC
		ProductParameter.Type.BOOL -> VIEW_TYPE_BOOL
		ProductParameter.Type.ENUM -> VIEW_TYPE_ENUM
	}
	
	override fun getItemId(item: ProductParameter) = ITEM_ID
}
