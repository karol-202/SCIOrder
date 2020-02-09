package pl.karol202.sciorder.client.android.admin.ui.adapter

import android.os.Handler
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.item_product_param.*
import kotlinx.android.synthetic.main.item_product_param_null.*
import pl.karol202.sciorder.client.android.admin.R
import pl.karol202.sciorder.client.android.common.ui.adapter.BasicAdapter
import pl.karol202.sciorder.client.android.common.ui.adapter.DynamicAdapter
import pl.karol202.sciorder.client.android.common.ui.addAfterTextChangedListener
import pl.karol202.sciorder.client.android.common.ui.setOnItemSelectedListener
import pl.karol202.sciorder.client.common.model.NEW_PARAMETER
import pl.karol202.sciorder.client.common.util.uuid
import pl.karol202.sciorder.common.model.Product
import pl.karol202.sciorder.common.model.duplicatedParameterNames

class ProductParamAdapter(initialParameters: List<Product.Parameter>,
                          private val onParamsUpdate: (List<Product.Parameter>) -> Unit) :
		DynamicAdapter<ProductParamAdapter.ParameterWithId?>()
{
	data class ParameterWithId(val id: String,
	                           var parameter: Product.Parameter)
	{
		companion object
		{
			val NULL: ParameterWithId? = null
		}
	}

	inner class ParamViewHolder(view: View) : BasicAdapter.ViewHolder<ParameterWithId?>(view)
	{
		private var item: ParameterWithId? = null
		
		private var ParameterWithId.name
			get() = parameter.name
			set(value)
			{
				val oldName = parameter.name
				if(value == oldName) return
				updateParameter(parameter.copy(name = value))
				updateParametersWithNames(oldName, value, excluding = this)
			}
		private var ParameterWithId.type
			get() = parameter.type
			set(value)
			{
				if(value == parameter.type) return
				updateParameter(parameter.copy(type = value, attributes = Product.Parameter.Attributes()))
				updateAttrsAdapter(this)
			}
		private var ParameterWithId.attributes
			get() = parameter.attributes
			set(value) = updateParameter(parameter.copy(attributes = value))

		init
		{
			editTextProductEditParamName.addAfterTextChangedListener { item?.name = it }

			spinnerProductEditParamType.adapter = ProductParamTypeAdapter()

			recyclerProductEditParamAttrs.layoutManager = LinearLayoutManager(ctx)
		}

		override fun bind(item: ParameterWithId?)
		{
			this.item = item ?: throw IllegalArgumentException()

			editTextProductEditParamName.setText(item.name)
			
			spinnerProductEditParamType.setOnItemSelectedListener { item.type = it as Product.Parameter.Type }
			spinnerProductEditParamType.setSelection(item.type.ordinal)

			buttonProductEditParamRemove.setOnClickListener { removeParam(item) }

			updateAttrsAdapter(item)
		}

		private fun updateAttrsAdapter(item: ParameterWithId)
		{
			recyclerProductEditParamAttrs.adapter = ProductParamAttrAdapter.fromParam(item.parameter) { item.attributes = it }
		}
		
		private fun ParameterWithId.updateParameter(parameter: Product.Parameter)
		{
			this.parameter = parameter
			validate(parameter)
			onParamsUpdate()
		}
		
		private fun validate(parameter: Product.Parameter) = with(parameter) {
			editLayoutProductEditParamName.error = when
			{
				!isNameValid -> ctx.getString(R.string.text_product_edit_param_name_no_value)
				name in parameters.duplicatedParameterNames -> ctx.getString(R.string.text_product_edit_param_name_duplicated)
				else -> null
			}
			when
			{
				!attributes.isMinimalValueValidFor(type) || !attributes.isMaximalValueValidFor(type) ->
					ctx.getString(R.string.text_product_edit_param_error_limits)
				!attributes.areEnumValuesValidFor(type) -> ctx.getString(R.string.text_product_edit_param_error_empty_enum)
				!attributes.isDefaultValueValidFor(type) -> ctx.getString(R.string.text_product_edit_param_error_default_value)
				else -> null
			}.let {
				textProductEditParamError.text = it
				textProductEditParamError.visibility = if(it != null) View.VISIBLE else View.GONE
			}
		}
	}

	inner class NullViewHolder(view: View) : BasicAdapter.ViewHolder<ParameterWithId?>(view)
	{
		override fun bind(item: ParameterWithId?)
		{
			viewProductEditParamNull.setOnClickListener { addNewParam() }
		}
	}

	companion object
	{
		private const val TYPE_PARAM = 0
		private const val TYPE_NULL = 1
	}

	private var parameters: List<Product.Parameter>
		get() = items.mapNotNull { it?.parameter }
		set(value) { items = value.map { it.withId() } + ParameterWithId.NULL }
	override var items: List<ParameterWithId?>
		get() = super.items
		set(value)
		{
			super.items = value
			onParamsUpdate()
		}
	
	init
	{
		parameters = initialParameters
	}

	private fun Product.Parameter.withId(id: String = uuid()) = ParameterWithId(id, this)

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

	override fun getItemId(item: ParameterWithId?) = item?.id ?: ""

	override fun getItemViewType(position: Int) = if(getItem(position) != null) TYPE_PARAM else TYPE_NULL

	private fun addNewParam()
	{
		val newItem = Product.Parameter.NEW_PARAMETER.withId()
		items = items.dropLast(1) + newItem + ParameterWithId.NULL
	}

	private fun removeParam(param: ParameterWithId)
	{
		items = items - param
		updateParametersWithNames(param.parameter.name)
	}

	private fun onParamsUpdate() = onParamsUpdate(parameters)
	
	private fun updateParametersWithNames(vararg names: String, excluding: ParameterWithId? = null) = Handler().post {
		items.withIndex().filter { (_, item) -> item != excluding && item?.parameter?.name in names }
						 .forEach { (i, _) -> notifyItemChanged(i) }
	}
}
