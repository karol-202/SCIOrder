package pl.karol202.sciorder.client.android.admin.ui.adapter

import android.text.InputType
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.item_product_param_attr_default_bool.*
import kotlinx.android.synthetic.main.item_product_param_attr_default_text.*
import kotlinx.android.synthetic.main.item_product_param_attr_enum.*
import kotlinx.android.synthetic.main.item_product_param_attr_range.*
import pl.karol202.sciorder.client.android.admin.R
import pl.karol202.sciorder.client.android.admin.ui.DividerItemDecorationWithoutLast
import pl.karol202.sciorder.client.common.extensions.addAfterTextChangedListener
import pl.karol202.sciorder.client.common.extensions.randomUUIDString
import pl.karol202.sciorder.client.common.ui.adapter.BasicAdapter
import pl.karol202.sciorder.client.common.ui.adapter.StaticAdapter
import pl.karol202.sciorder.common.Product

class ProductParamAttrAdapter(private val attrs: List<Attr>,
                              private val attrsUpdateListener: (Product.Parameter.Attributes) -> Unit) :
		StaticAdapter<ProductParamAttrAdapter.Attr>(attrs)
{
	sealed class Attr(val viewType: Int)
	{
		sealed class DefaultValue(viewType: kotlin.Int) : Attr(viewType)
		{
			interface Textual
			{
				var defaultAsString: String?

				val inputType: kotlin.Int
			}

			data class Text(var default: String?) : DefaultValue(VIEW_TYPE_DEFAULT_TEXT), Textual
			{
				override var defaultAsString: String?
					get() = default
					set(value) { default = value }

				override val inputType = InputType.TYPE_CLASS_TEXT

				override fun applyTo(attributes: Product.Parameter.Attributes) =
						attributes.copy(defaultValue = default)
			}

			data class Int(var default: kotlin.Int?) : DefaultValue(VIEW_TYPE_DEFAULT_TEXT), Textual
			{
				override var defaultAsString: String?
					get() = default?.toString()
					set(value) { default = value?.toIntOrNull() }

				override val inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED

				override fun applyTo(attributes: Product.Parameter.Attributes) =
						attributes.copy(defaultValue = defaultAsString)
			}

			data class Float(var default: kotlin.Float?) : DefaultValue(VIEW_TYPE_DEFAULT_TEXT), Textual
			{
				override var defaultAsString: String?
					get() = default?.toString()
					set(value) { default = value?.toFloatOrNull() }

				override val inputType = InputType.TYPE_CLASS_NUMBER or
						InputType.TYPE_NUMBER_FLAG_SIGNED or
						InputType.TYPE_NUMBER_FLAG_DECIMAL

				override fun applyTo(attributes: Product.Parameter.Attributes) =
						attributes.copy(defaultValue = defaultAsString)
			}

			data class Bool(var default: Boolean) : DefaultValue(VIEW_TYPE_DEFAULT_BOOL)
			{
				override fun applyTo(attributes: Product.Parameter.Attributes) =
						attributes.copy(defaultValue = default.toString())
			}
		}

		sealed class Range : Attr(VIEW_TYPE_RANGE)
		{
			data class Int(var minimum: kotlin.Int?,
			               var maximum: kotlin.Int?) : Range()
			{
				override val inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED

				override var minimumAsString: String?
					get() = minimum?.toString()
					set(value) { minimum = value?.toIntOrNull() }
				override var maximumAsString: String?
					get() = maximum?.toString()
					set(value) { maximum = value?.toIntOrNull() }

				override fun applyTo(attributes: Product.Parameter.Attributes) =
						attributes.copy(minimalValue = minimum?.toFloat(), maximalValue = maximum?.toFloat())
			}

			data class Float(var minimum: kotlin.Float?,
			                 var maximum: kotlin.Float?) : Range()
			{
				override val inputType = InputType.TYPE_CLASS_NUMBER or
						InputType.TYPE_NUMBER_FLAG_SIGNED or
						InputType.TYPE_NUMBER_FLAG_DECIMAL

				override var minimumAsString: String?
					get() = minimum?.toString()
					set(value) { minimum = value?.toFloatOrNull() }
				override var maximumAsString: String?
					get() = maximum?.toString()
					set(value) { maximum = value?.toFloatOrNull() }

				override fun applyTo(attributes: Product.Parameter.Attributes) =
						attributes.copy(minimalValue = minimum, maximalValue = maximum)
			}

			abstract val inputType: kotlin.Int

			abstract var minimumAsString: String?
			abstract var maximumAsString: String?
		}

		data class Enum(var values: List<String>,
		                var defaultValue: String?) : Attr(VIEW_TYPE_ENUM)
		{
			override fun applyTo(attributes: Product.Parameter.Attributes) =
					attributes.copy(enumValues = values, defaultValue = defaultValue)
		}

		val id = randomUUIDString()

		abstract fun applyTo(attributes: Product.Parameter.Attributes): Product.Parameter.Attributes
	}

	companion object
	{
		private const val VIEW_TYPE_DEFAULT_TEXT = 0
		private const val VIEW_TYPE_DEFAULT_BOOL = 1
		private const val VIEW_TYPE_RANGE = 2
		private const val VIEW_TYPE_ENUM = 3

		fun fromParam(param: Product.Parameter, attrsUpdateListener: (Product.Parameter.Attributes) -> Unit) =
				ProductParamAttrAdapter(createAttrs(param), attrsUpdateListener)

		private fun createAttrs(param: Product.Parameter) = when(param.type)
		{
			Product.Parameter.Type.TEXT -> listOf(Attr.DefaultValue.Text(param.attributes.defaultValue))
			Product.Parameter.Type.INT -> listOf(Attr.DefaultValue.Int(param.attributes.defaultValue?.toIntOrNull()),
			                                     Attr.Range.Int(param.attributes.minimalValue?.toInt(), param.attributes.maximalValue?.toInt()))
			Product.Parameter.Type.FLOAT -> listOf(Attr.DefaultValue.Float(param.attributes.defaultValue?.toFloatOrNull()),
			                                       Attr.Range.Float(param.attributes.minimalValue, param.attributes.maximalValue))
			Product.Parameter.Type.BOOL -> listOf(Attr.DefaultValue.Bool(param.attributes.defaultValue?.toBoolean() ?: false))
			Product.Parameter.Type.ENUM -> listOf(Attr.Enum(param.attributes.enumValues ?: listOf(),
			                                                param.attributes.defaultValue))
		}
	}

	inner class ViewHolderDefaultText(view: View) : BasicAdapter.ViewHolder<Attr>(view)
	{
		private var item: Attr.DefaultValue.Textual? = null

		init
		{
			editTextProductEditParamAttrDefaultText.addAfterTextChangedListener { setDefaultValue(it) }
		}

		override fun bind(item: Attr)
		{
			val textualItem = item as Attr.DefaultValue.Textual
			this.item = textualItem

			editTextProductEditParamAttrDefaultText.setText(textualItem.defaultAsString)
			editTextProductEditParamAttrDefaultText.inputType = textualItem.inputType
		}

		private fun setDefaultValue(value: String?)
		{
			item?.defaultAsString = value?.takeIf { it.isNotBlank() }
			onAttrsUpdated()
		}
	}

	inner class ViewHolderDefaultBool(view: View) : BasicAdapter.ViewHolder<Attr>(view)
	{
		override fun bind(item: Attr)
		{
			val boolItem = item as Attr.DefaultValue.Bool

			checkProductEditParamAttrDefaultBool.setOnCheckedChangeListener { _, checked -> setDefaultValue(boolItem, checked) }
			checkProductEditParamAttrDefaultBool.isChecked = boolItem.default
		}

		private fun setDefaultValue(item: Attr.DefaultValue.Bool, value: Boolean)
		{
			item.default = value
			onAttrsUpdated()
		}
	}

	inner class ViewHolderRange(view: View) : BasicAdapter.ViewHolder<Attr>(view)
	{
		private var item: Attr.Range? = null

		init
		{
			editTextProductEditParamAttrMin.addAfterTextChangedListener { setMinimum(it) }
			editTextProductEditParamAttrMax.addAfterTextChangedListener { setMaximum(it) }
		}

		override fun bind(item: Attr)
		{
			val rangeItem = item as Attr.Range
			this.item = rangeItem

			editTextProductEditParamAttrMin.setText(rangeItem.minimumAsString)
			editTextProductEditParamAttrMax.setText(rangeItem.maximumAsString)

			editTextProductEditParamAttrMin.inputType = item.inputType
			editTextProductEditParamAttrMax.inputType = item.inputType
		}

		private fun setMinimum(minimum: String?)
		{
			item?.minimumAsString = minimum
			onAttrsUpdated()
		}

		private fun setMaximum(maximum: String?)
		{
			item?.maximumAsString = maximum
			onAttrsUpdated()
		}
	}

	inner class ViewHolderEnum(view: View) : BasicAdapter.ViewHolder<Attr>(view)
	{
		private var item: Attr.Enum? = null

		private val adapter = ProductParamAttrEnumValuesAdapter(ctx) { values, default -> setEnumValuesAndDefault(values, default) }

		init
		{
			recyclerProductEditParamAttrEnum.layoutManager = LinearLayoutManager(ctx)
			recyclerProductEditParamAttrEnum.adapter = adapter
			recyclerProductEditParamAttrEnum.addItemDecoration(DividerItemDecorationWithoutLast(ctx))
		}

		override fun bind(item: Attr)
		{
			this.item = item as Attr.Enum

			adapter.values = item.values
			adapter.selection = item.defaultValue
		}

		private fun setEnumValuesAndDefault(values: List<String>, default: String?)
		{
			item?.values = values
			item?.defaultValue = default
			onAttrsUpdated()
		}
	}

	override fun getLayout(viewType: Int) = when(viewType)
	{
		VIEW_TYPE_DEFAULT_TEXT -> R.layout.item_product_param_attr_default_text
		VIEW_TYPE_DEFAULT_BOOL -> R.layout.item_product_param_attr_default_bool
		VIEW_TYPE_RANGE -> R.layout.item_product_param_attr_range
		VIEW_TYPE_ENUM -> R.layout.item_product_param_attr_enum
		else -> throw IllegalArgumentException()
	}

	override fun createViewHolder(view: View, viewType: Int) = when(viewType)
	{
		VIEW_TYPE_DEFAULT_TEXT -> ViewHolderDefaultText(view)
		VIEW_TYPE_DEFAULT_BOOL -> ViewHolderDefaultBool(view)
		VIEW_TYPE_RANGE -> ViewHolderRange(view)
		VIEW_TYPE_ENUM -> ViewHolderEnum(view)
		else -> throw IllegalArgumentException()
	}

	override fun getItemViewType(position: Int) = getItem(position).viewType

	private fun onAttrsUpdated() = attrsUpdateListener(createParamAttrs())

	private fun createParamAttrs(): Product.Parameter.Attributes
	{
		var attributes = Product.Parameter.Attributes()
		attrs.forEach { attributes = it.applyTo(attributes) }
		return attributes
	}
}
