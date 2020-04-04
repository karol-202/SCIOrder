package pl.karol202.sciorder.client.android.admin.ui.adapter

import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.item_product_param.*
import kotlinx.android.synthetic.main.item_product_param_null.*
import pl.karol202.sciorder.client.android.admin.R
import pl.karol202.sciorder.client.android.common.ui.adapter.BasicAdapter
import pl.karol202.sciorder.client.android.common.ui.adapter.DynamicAdapter
import pl.karol202.sciorder.client.android.common.ui.setOnItemSelectedListener
import pl.karol202.sciorder.common.model.ProductParameter
import pl.karol202.sciorder.common.validation.*

class ProductParamAdapter(private val onParamAdd: () -> Unit,
                          private val onParamEdit: (ProductParameter) -> Unit,
                          private val onParamRemove: (Long) -> Unit) : DynamicAdapter<ProductParameter?>()
{
	inner class ParamViewHolder(view: View) : BasicAdapter.ViewHolder<ProductParameter?>(view)
	{
		private var parameter: ProductParameter? = null

		init
		{
			editLayoutProductEditParamName.counterMaxLength = ProductParameter.MAX_NAME_LENGTH
			editTextProductEditParamName.doAfterTextChanged { updateParameterName(it.toString()) }

			spinnerProductEditParamType.adapter = ProductParamTypeAdapter()

			recyclerProductEditParamAttrs.layoutManager = LinearLayoutManager(ctx)
		}

		override fun bind(item: ProductParameter?)
		{
			this.parameter = item ?: throw IllegalArgumentException()

			editTextProductEditParamName.setText(item.name)
			
			spinnerProductEditParamType.setOnItemSelectedListener { updateParameterType(it as ProductParameter.Type) }
			spinnerProductEditParamType.setSelection(item.type.ordinal)

			buttonProductEditParamRemove.setOnClickListener { onParamRemove(item.id) }
			
			recyclerProductEditParamAttrs.adapter = ProductParamAttrAdapter.fromParam(item) { updateParameterAttributes(it) }
			
			validate(item)
		}
		
		private fun validate(parameter: ProductParameter) = with(parameter.toRequest) {
			editLayoutProductEditParamName.error = when
			{
				!isNameNotBlank -> ctx.getString(R.string.text_product_edit_param_name_no_value)
				!isNameShortEnough -> ctx.getString(R.string.text_product_edit_param_name_too_long)
				else -> null
			}
			
			val errorText = when
			{
				!attributes.isMinimalValueValidFor(type) || !attributes.isMaximalValueValidFor(type) ->
					ctx.getString(R.string.text_product_edit_param_error_limits)
				!attributes.areEnumValuesValidFor(type) -> ctx.getString(R.string.text_product_edit_param_error_empty_enum)
				!attributes.isDefaultValueValidFor(type) -> ctx.getString(R.string.text_product_edit_param_error_default_value)
				else -> null
			}
			textProductEditParamError.text = errorText
			textProductEditParamError.visibility = if(errorText != null) View.VISIBLE else View.GONE
		}
		
		private fun updateParameterName(name: String)
		{
			val parameter = parameter ?: return
			onParamEdit(parameter.copy(name = name))
		}
		
		private fun updateParameterType(type: ProductParameter.Type)
		{
			val parameter = parameter ?: return
			onParamEdit(parameter.copy(type = type, attributes = ProductParameter.Attributes()))
		}
		
		private fun updateParameterAttributes(attributes: ProductParameter.Attributes)
		{
			val parameter = parameter ?: return
			onParamEdit(parameter.copy(attributes = attributes))
		}
	}

	inner class NullViewHolder(view: View) : BasicAdapter.ViewHolder<ProductParameter?>(view)
	{
		override fun bind(item: ProductParameter?)
		{
			viewProductEditParamNull.setOnClickListener { onParamAdd() }
		}
	}

	companion object
	{
		private const val TYPE_PARAM = 0
		private const val TYPE_NULL = 1
		
		private const val NULL_ITEM_ID = -1
	}

	var parameters: List<ProductParameter>
		get() = items.filterNotNull()
		set(value) { items = value + null }

	override fun getLayout(viewType: Int) = when(viewType)
	{
		TYPE_PARAM -> R.layout.item_product_param
		TYPE_NULL -> R.layout.item_product_param_null
		else -> throw IllegalArgumentException()
	}

	override fun createViewHolder(view: View, viewType: Int) = when(viewType)
	{
		TYPE_PARAM -> ParamViewHolder(view)
		TYPE_NULL -> NullViewHolder(view)
		else -> throw IllegalArgumentException()
	}

	override fun getItemId(item: ProductParameter?) = item?.id ?: NULL_ITEM_ID

	override fun getItemViewType(position: Int) = if(getItem(position) != null) TYPE_PARAM else TYPE_NULL
}
