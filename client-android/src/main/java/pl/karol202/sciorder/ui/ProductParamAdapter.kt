package pl.karol202.sciorder.ui

import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
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
import pl.karol202.sciorder.R
import pl.karol202.sciorder.extensions.ctx
import pl.karol202.sciorder.model.Product

class ProductParamAdapter(val product: Product) : RecyclerView.Adapter<ProductParamAdapter.ViewHolder>()
{
	abstract class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer
	{
		protected val ctx get() = containerView.ctx

		abstract val textName: TextView

		open fun bind(param: Product.Parameter)
		{
			textName.text = param.name
		}
	}

	open class ViewHolderText(containerView: View) : ViewHolder(containerView)
	{
		override val textName: TextView = textProductParamTextName
	}

	abstract class ViewHolderNumber<N : Number>(containerView: View) : ViewHolderText(containerView)
	{
		override fun bind(param: Product.Parameter)
		{
			super.bind(param)
			editTextProductParamText.inputType = getInputType()
			editTextProductParamText.addTextChangedListener(object : TextWatcher {
				override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

				override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }

				override fun afterTextChanged(s: Editable?) = updateError(param)
			})
			updateError(param)
		}

		protected abstract fun getInputType(): Int

		private fun updateError(param: Product.Parameter)
		{
			editLayoutProductParamText.error = if(!isValid(param)) param.attributes.getRangeText() else null
		}

		private fun isValid(param: Product.Parameter) =
			getValue()?.toFloat()?.let { it in param.attributes.getRange() } ?: false

		private fun getValue() = editTextProductParamText.text?.toString()?.let { convertValue(it) }

		protected abstract fun convertValue(string: String): N?

		private fun Product.Parameter.Attributes.getRange(): ClosedFloatingPointRange<Float>
		{
			val minValue = minimalValue ?: Float.MIN_VALUE
			val maxValue = maximalValue ?: Float.MAX_VALUE
			return minValue..maxValue
		}

		protected abstract fun Product.Parameter.Attributes.getRangeText(): String
	}

	class ViewHolderInt(containerView: View) : ViewHolderNumber<Int>(containerView)
	{
		override fun getInputType() = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED

		override fun convertValue(string: String) = string.toIntOrNull()

		override fun Product.Parameter.Attributes.getRangeText(): String = when
		{
			minimalValue != null && maximalValue != null ->
				ctx.getString(R.string.text_product_param_int_range_closed, minimalValue?.toInt(), maximalValue?.toInt())
			minimalValue != null -> ctx.getString(R.string.text_product_param_int_range_left_closed, minimalValue?.toInt())
			maximalValue != null -> ctx.getString(R.string.text_product_param_int_range_right_closed, maximalValue?.toInt())
			else -> ""
		}
	}

	class ViewHolderFloat(containerView: View) : ViewHolderNumber<Float>(containerView)
	{
		override fun getInputType() =
			InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED or InputType.TYPE_NUMBER_FLAG_DECIMAL

		override fun convertValue(string: String) = string.toFloatOrNull()

		override fun Product.Parameter.Attributes.getRangeText(): String = when
		{
			minimalValue != null && maximalValue != null ->
				ctx.getString(R.string.text_product_param_float_range_closed, minimalValue, maximalValue)
			minimalValue != null -> ctx.getString(R.string.text_product_param_float_range_left_closed, minimalValue)
			maximalValue != null -> ctx.getString(R.string.text_product_param_float_range_right_closed, maximalValue)
			else -> ""
		}
	}

	class ViewHolderBool(containerView: View) : ViewHolder(containerView)
	{
		override val textName: TextView = textProductParamBoolName
	}

	class ViewHolderEnum(containerView: View) : ViewHolder(containerView)
	{
		override val textName: TextView = textProductParamEnumName

		override fun bind(param: Product.Parameter)
		{
			super.bind(param)
			spinnerProductParamEnum.adapter = ArrayAdapter<String>(ctx,
																   R.layout.item_product_param_enum_value,
																   R.id.textProductParamEnumValue,
																   param.attributes.enumValues ?: emptyList())
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

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
	{
		fun inflateView(@LayoutRes layout: Int) = LayoutInflater.from(parent.ctx).inflate(layout, parent, false)

		val viewTypeEnum = ViewType.values()[viewType]
		return viewTypeEnum.viewHolderCreator(inflateView(viewTypeEnum.layout))
	}

	override fun getItemCount() = product.parameters.size

	override fun onBindViewHolder(holder: ViewHolder, position: Int)
	{
		holder.bind(product.parameters[position])
	}

	override fun getItemViewType(position: Int) = ViewType[product.parameters[position].type].ordinal
}
