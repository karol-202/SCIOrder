package pl.karol202.sciorder.client.admin.ui.adapter

import android.view.View
import kotlinx.android.synthetic.main.item_product_param.*
import kotlinx.android.synthetic.main.item_product_param_null.*
import pl.karol202.sciorder.client.admin.R
import pl.karol202.sciorder.client.common.extensions.addAfterTextChangedListener
import pl.karol202.sciorder.client.common.extensions.randomUUIDString
import pl.karol202.sciorder.client.common.extensions.setOnItemSelectedListener
import pl.karol202.sciorder.client.common.ui.adapter.BasicAdapter
import pl.karol202.sciorder.client.common.ui.adapter.DynamicAdapter
import pl.karol202.sciorder.common.model.Product

class ProductParamAdapter : DynamicAdapter<ProductParamAdapter.ParameterWithId?>()
{
	data class ParameterWithId(val id: String,
	                           var parameter: Product.Parameter)
	{
		companion object
		{
			val NULL: ParameterWithId? = null
		}

		var name
			get() = parameter.name
			set(value) { parameter = parameter.copy(name = value) }
		var type
			get() = parameter.type
			set(value) { parameter = parameter.copy(type = value) }
		var attributes
			get() = parameter.attributes
			set(value) { parameter = parameter.copy(attributes = value) }
	}

	inner class ParamViewHolder(view: View) : BasicAdapter.ViewHolder<ParameterWithId?>(view)
	{
		private var item: ParameterWithId? = null

		init
		{
			editTextProductEditParamName.addAfterTextChangedListener { onNameChanged(it) }

			spinnerProductEditParamType.adapter = ProductParamTypeAdapter()
		}

		override fun bind(item: ParameterWithId?)
		{
			this.item = item ?: throw IllegalArgumentException()

			editTextProductEditParamName.setText(item.name)

			spinnerProductEditParamType.setOnItemSelectedListener { onTypeChanged(it as Product.Parameter.Type) }
			spinnerProductEditParamType.setSelection(item.type.ordinal)

			buttonProductEditParamRemove.setOnClickListener { removeParam(item) }
		}

		private fun onNameChanged(name: String?)
		{
			item?.name = name ?: ""
			updateNameError(name)
		}

		private fun updateNameError(name: String?)
		{
			editLayoutProductEditParamName.error =
					if(name.isNullOrBlank()) ctx.getString(R.string.text_product_edit_param_name_no_value) else ""
		}

		private fun onTypeChanged(type: Product.Parameter.Type)
		{
			item?.type = type
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

	var parameters: List<Product.Parameter>
		get() = items.mapNotNull { it?.parameter }
		set(value) { items = value.map { it.withId() } + ParameterWithId.NULL }

	init
	{
		parameters = emptyList() // Invokes setter in order for NULL item to be added
	}

	private fun Product.Parameter.withId(id: String = randomUUIDString()) = ParameterWithId(id, this)

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
		val newItem = Product.Parameter("", Product.Parameter.Type.TEXT, Product.Parameter.Attributes()).withId()
		items = items.dropLast(1) + newItem + ParameterWithId.NULL
	}

	private fun removeParam(param: ParameterWithId)
	{
		items = items - param
	}
}
