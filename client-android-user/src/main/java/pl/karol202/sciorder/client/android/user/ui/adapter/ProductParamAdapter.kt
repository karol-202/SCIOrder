package pl.karol202.sciorder.client.android.user.ui.adapter

import android.content.Context
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_product_param_bool.*
import kotlinx.android.synthetic.main.item_product_param_enum.*
import kotlinx.android.synthetic.main.item_product_param_text.*
import pl.karol202.sciorder.client.android.common.extensions.addAfterTextChangedListener
import pl.karol202.sciorder.client.android.common.extensions.ctx
import pl.karol202.sciorder.client.android.common.extensions.setOnItemSelectedListener
import pl.karol202.sciorder.client.android.user.R
import pl.karol202.sciorder.common.model.Product

class ProductParamAdapter(context: Context,
                          product: Product,
                          initialQuantity: Int) : RecyclerView.Adapter<ProductParamAdapter.ViewHolder>()
{
	abstract class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer
	{
		protected val ctx get() = containerView.ctx

		abstract val textName: TextView

		protected var param: Product.Parameter? = null
		protected var onUpdateListener: ((Any?) -> Unit)? = null

		open fun bind(param: Product.Parameter, onUpdateListener: (Any?) -> Unit)
		{
			this.param = param
			this.onUpdateListener = onUpdateListener

			textName.text = param.name
		}
	}

	abstract class ViewHolderWithEditText(containerView: View) : ViewHolder(containerView)
	{
		override val textName: TextView = textProductParamTextName

		override fun bind(param: Product.Parameter, onUpdateListener: (Any?) -> Unit)
		{
			super.bind(param, onUpdateListener)
			editTextProductParamText.setText(param.attributes.defaultValue ?: "")
		}
	}

	class ViewHolderText(containerView: View) : ViewHolderWithEditText(containerView)
	{
		init
		{
			editTextProductParamText.addAfterTextChangedListener { onUpdateListener?.invoke(it ?: "") }
		}
	}

	abstract class ViewHolderNumber<T : Number>(containerView: View,
	                                            inputType: Int) : ViewHolderWithEditText(containerView)
	{
		init
		{
			editTextProductParamText.inputType = inputType
			editTextProductParamText.addAfterTextChangedListener {
				updateError()
				onUpdateListener?.invoke(param?.let { getValidValue(it) })
			}
		}

		override fun bind(param: Product.Parameter, onUpdateListener: (Any?) -> Unit)
		{
			super.bind(param, onUpdateListener)
			updateError()
		}

		private fun updateError()
		{
			editLayoutProductParamText.error = param?.let { if(getValidValue(it) == null) it.attributes.getErrorText() else null }
		}

		private fun getValidValue(param: Product.Parameter): T?
		{
			val minValue = param.attributes.minimalValue ?: Float.NEGATIVE_INFINITY
			val maxValue = param.attributes.maximalValue ?: Float.POSITIVE_INFINITY
			val range = minValue..maxValue

			val value = editTextProductParamText.text?.toString()?.let { convertValue(it) }
			return value?.takeIf { it.toFloat() in range }
		}

		protected abstract fun convertValue(string: String?): T?

		protected abstract fun Product.Parameter.Attributes.getErrorText(): String
	}

	class ViewHolderInt(containerView: View) : ViewHolderNumber<Int>(containerView, INPUT_TYPE)
	{
		companion object
		{
			private const val INPUT_TYPE = InputType.TYPE_CLASS_NUMBER or
					InputType.TYPE_NUMBER_FLAG_SIGNED
		}

		override fun convertValue(string: String?) = string?.toIntOrNull()

		override fun Product.Parameter.Attributes.getErrorText(): String = when
		{
			minimalValue != null && maximalValue != null ->
				ctx.getString(R.string.text_product_param_int_range_closed, minimalValue?.toInt(), maximalValue?.toInt())
			minimalValue != null -> ctx.getString(R.string.text_product_param_int_range_left_closed, minimalValue?.toInt())
			maximalValue != null -> ctx.getString(R.string.text_product_param_int_range_right_closed, maximalValue?.toInt())
			else -> ctx.getString(R.string.text_product_param_no_value)
		}
	}

	class ViewHolderFloat(containerView: View) : ViewHolderNumber<Float>(containerView, INPUT_TYPE)
	{
		companion object
		{
			private const val INPUT_TYPE = InputType.TYPE_CLASS_NUMBER or
					InputType.TYPE_NUMBER_FLAG_SIGNED or
					InputType.TYPE_NUMBER_FLAG_DECIMAL
		}

		override fun convertValue(string: String?) = string?.toFloatOrNull()

		override fun Product.Parameter.Attributes.getErrorText(): String = when
		{
			minimalValue != null && maximalValue != null ->
				ctx.getString(R.string.text_product_param_float_range_closed, minimalValue, maximalValue)
			minimalValue != null -> ctx.getString(R.string.text_product_param_float_range_left_closed, minimalValue)
			maximalValue != null -> ctx.getString(R.string.text_product_param_float_range_right_closed, maximalValue)
			else -> ctx.getString(R.string.text_product_param_no_value)
		}
	}

	class ViewHolderBool(containerView: View) : ViewHolder(containerView)
	{
		override val textName: TextView = textProductParamBoolName

		init
		{
			checkProductParamBool.setOnCheckedChangeListener { _, checked -> invokeListener(checked) }
		}

		override fun bind(param: Product.Parameter, onUpdateListener: (Any?) -> Unit)
		{
			super.bind(param, onUpdateListener)
			val value = param.attributes.defaultValue?.toBoolean() ?: false

			checkProductParamBool.isChecked = value
			invokeListener(value)
		}

		private fun invokeListener(value: Boolean) = onUpdateListener?.invoke(value)
	}

	class ViewHolderEnum(containerView: View) : ViewHolder(containerView)
	{
		override val textName: TextView = textProductParamEnumName

		init
		{
			spinnerProductParamEnum.setOnItemSelectedListener { onUpdateListener?.invoke(it) }
		}

		override fun bind(param: Product.Parameter, onUpdateListener: (Any?) -> Unit)
		{
			super.bind(param, onUpdateListener)
			val values = param.attributes.enumValues ?: emptyList()
			spinnerProductParamEnum.adapter = ArrayAdapter<String>(ctx,
			                                                       R.layout.item_product_param_enum_value,
			                                                       R.id.textProductParamEnumValue,
			                                                       values)
			spinnerProductParamEnum.setSelection(values.indexOf(param.attributes.defaultValue))
		}
	}

	enum class ViewType(val parameterType: Product.Parameter.Type,
	                    @LayoutRes val layout: Int,
	                    val viewHolderCreator: (View) -> ViewHolder)
	{
		TYPE_TEXT(Product.Parameter.Type.TEXT, R.layout.item_product_param_text, { ViewHolderText(it) }),
		TYPE_INT(Product.Parameter.Type.INT, R.layout.item_product_param_text, { ViewHolderInt(it) }),
		TYPE_FLOAT(Product.Parameter.Type.FLOAT, R.layout.item_product_param_text, { ViewHolderFloat(it) }),
		TYPE_BOOL(Product.Parameter.Type.BOOL, R.layout.item_product_param_bool, { ViewHolderBool(it) }),
		TYPE_ENUM(Product.Parameter.Type.ENUM, R.layout.item_product_param_enum, { ViewHolderEnum(it) });

		companion object
		{
			operator fun get(parameterType: Product.Parameter.Type) = values().single { it.parameterType == parameterType }
		}
	}

	private data class ParamWithValue(val param: Product.Parameter,
	                                  var value: Any?)

	private val allParams = product.parameters + Product.Parameter(context.getString(R.string.text_quantity),
	                                                               Product.Parameter.Type.INT,
	                                                               Product.Parameter.Attributes(minimalValue = 1f,
	                                                                                            defaultValue = initialQuantity.toString()))
	private val items = allParams.map { ParamWithValue(it, null) }

	val quantity get() = items.last().value as? Int
	val params get() = items.dropLast(1).associate { it.param to it.value }

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
	{
		fun inflateView(@LayoutRes layout: Int) = LayoutInflater.from(parent.ctx).inflate(layout, parent, false)

		val viewTypeEnum = ViewType.values()[viewType]
		return viewTypeEnum.viewHolderCreator(inflateView(viewTypeEnum.layout))
	}

	override fun getItemCount() = items.size

	override fun onBindViewHolder(holder: ViewHolder, position: Int)
	{
		val paramWithValue = items[position]
		holder.bind(paramWithValue.param) { paramWithValue.value = it }
	}

	override fun getItemViewType(position: Int) = ViewType[items[position].param.type].ordinal
}
