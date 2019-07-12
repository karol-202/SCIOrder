package pl.karol202.sciorder.client.js.common.view

import com.ccfraser.muirwik.components.MColor
import com.ccfraser.muirwik.components.MTypographyVariant
import com.ccfraser.muirwik.components.list.MListItemAlignItems
import com.ccfraser.muirwik.components.list.mList
import com.ccfraser.muirwik.components.list.mListItem
import com.ccfraser.muirwik.components.mButton
import com.ccfraser.muirwik.components.mCheckbox
import com.ccfraser.muirwik.components.mMenuItem
import com.ccfraser.muirwik.components.mTextField
import com.ccfraser.muirwik.components.mTextFieldSelect
import com.ccfraser.muirwik.components.mTypography
import com.ccfraser.muirwik.components.targetInputValue
import com.ccfraser.muirwik.components.targetValue
import kotlinext.js.jsObject
import kotlinx.css.FlexDirection
import kotlinx.css.flexShrink
import kotlinx.css.margin
import kotlinx.css.marginRight
import kotlinx.css.marginTop
import kotlinx.css.px
import kotlinx.html.InputType
import pl.karol202.sciorder.client.common.model.OrderedProduct
import pl.karol202.sciorder.client.js.common.model.create
import pl.karol202.sciorder.client.js.common.util.flexBox
import pl.karol202.sciorder.client.js.common.util.prop
import pl.karol202.sciorder.common.Product
import react.RBuilder
import react.RProps
import react.RState
import react.key
import react.setState
import styled.css

class ProductOrderView(props: Props) : View<ProductOrderView.Props, ProductOrderView.State>(props)
{
	interface Props : RProps
	{
		var product: Product?
		var onOrder: ((OrderedProduct) -> Unit)?
		var onAddToOrder: ((OrderedProduct) -> Unit)?
	}

	interface State : RState
	{
		var params: Map<Product.Parameter, String>
		var quantity: String
	}

	private val quantityAsParameter = Product.Parameter("Liczba",
	                                                    Product.Parameter.Type.INT,
	                                                    Product.Parameter.Attributes(minimalValue = 1f, defaultValue = "1"))

	private val product by prop { product }
	private val onOrder by prop { onOrder }
	private val onAddToOrder by prop { onAddToOrder }

	init
	{
		state.params = product.parameters.associateWith { it.attributes.defaultValue ?: "" }
		state.quantity = "1"
	}

	override fun RBuilder.render()
	{
		flexBox(flexDirection = FlexDirection.column) {
			itemsList()
			addToOrderButton()
			orderButton()
		}
	}

	private fun RBuilder.itemsList() = mList {
		product.parameters.forEach { paramItem(it) }
		quantityItem()
	}

	private fun RBuilder.paramItem(param: Product.Parameter) = when(param.type)
	{
		Product.Parameter.Type.TEXT -> textItem(param, state.params.getValue(param)) { setParam(param, it) }
		Product.Parameter.Type.INT -> intItem(param, state.params.getValue(param)) { setParam(param, it) }
		Product.Parameter.Type.FLOAT -> floatItem(param, state.params.getValue(param)) { setParam(param, it) }
		Product.Parameter.Type.BOOL -> booleanItem(param, state.params.getValue(param)) { setParam(param, it) }
		Product.Parameter.Type.ENUM -> enumItem(param, state.params.getValue(param)) { setParam(param, it) }
	}

	private fun RBuilder.quantityItem() = intItem(quantityAsParameter, state.quantity) { setQuantity(it) }

	private fun RBuilder.textItem(param: Product.Parameter, value: String, onUpdate: (String) -> Unit) = item(param) {
		mTextField(label = "",
		           fullWidth = true,
		           value = value,
		           onChange = { onUpdate(it.targetInputValue) })
	}

	private fun RBuilder.intItem(param: Product.Parameter, value: String, onUpdate: (String) -> Unit) =
			numberItem(param, { toIntOrNull() }, false, value, onUpdate)

	private fun RBuilder.floatItem(param: Product.Parameter, value: String, onUpdate: (String) -> Unit) =
			numberItem(param, { toFloatOrNull() }, true, value, onUpdate)

	private fun RBuilder.numberItem(param: Product.Parameter,
	                                convert: String.() -> Number?,
	                                anyStep: Boolean,
	                                value: String,
	                                onUpdate: (String) -> Unit) = item(param) {
		val error = value.takeIfValidNumber(param, convert) == null
		val errorText = when
		{
			!error -> ""
			param.attributes.minimalValue != null && param.attributes.maximalValue != null ->
				"Od ${param.attributes.minimalValue} do ${param.attributes.maximalValue}"
			param.attributes.minimalValue != null -> "Minimalnie ${param.attributes.minimalValue}"
			param.attributes.maximalValue != null -> "Maksymalnie ${param.attributes.maximalValue}"
			else -> "Podaj wartość"
		}

		mTextField(label = "",
		           type = InputType.number,
		           fullWidth = true,
		           helperText = errorText,
		           error = error,
		           nativeInputProps = nativeInputProps(param.attributes.minimalValue, param.attributes.maximalValue, anyStep),
		           value = value,
		           onChange = { onUpdate(it.targetInputValue) })
	}

	private fun RBuilder.booleanItem(param: Product.Parameter, value: String, onUpdate: (String) -> Unit) = item(param) {
		mCheckbox(primary = false,
		          checked = value.toBoolean(),
		          onChange = { _, checked -> onUpdate(checked.toString()) })
	}

	private fun RBuilder.enumItem(param: Product.Parameter, value: String, onUpdate: (String) -> Unit) = item(param) {
		mTextFieldSelect(label = "",
		                 fullWidth = true,
		                 value = value,
		                 onChange = { onUpdate(it.targetValue.toString()) }) {
			param.attributes.enumValues?.forEach {
				mMenuItem(primaryText = it, value = it)
			}
		}
	}

	private fun RBuilder.item(param: Product.Parameter, handler: RBuilder.() -> Unit) =
			mListItem(alignItems = MListItemAlignItems.flexStart) {
				mTypography(param.name, variant = MTypographyVariant.body2) {
					css {
						specific {
							marginTop = 24.px
							marginRight = 24.px
						}
						flexShrink = 0.0
					}
				}
				handler()
			}

	private fun nativeInputProps(min: Float?, max: Float?, anyStep: Boolean) = jsObject<dynamic> {
		min?.let { this.min = it }
		max?.let { this.max = it }
		if(anyStep) step = "any"
	}.unsafeCast<RProps>()

	private fun RBuilder.addToOrderButton() = button(text = "Dodaj do zamówienia",
	                                                 onClick = { addToOrder() })

	private fun RBuilder.orderButton() = button(text = "Zamów",
	                                            onClick = { order() })

	private fun RBuilder.button(text: String,
	                            onClick: () -> Unit) = mButton(caption = text,
	                                                           color = MColor.secondary,
	                                                           onClick = { onClick() }) {
		css {
			specific {
				margin(left = 16.px, right = 16.px, bottom = 16.px)
			}
		}
	}

	private fun setParam(param: Product.Parameter, value: String) = setState { params += param to value }

	private fun setQuantity(quantity: String) = setState { this.quantity = quantity }

	private fun order() = createOrderedProduct()?.let(onOrder)

	private fun addToOrder() = createOrderedProduct()?.let(onAddToOrder)

	private fun createOrderedProduct(): OrderedProduct?
	{
		val quantity = state.quantity.takeIfValidInt(quantityAsParameter) ?: return null
		val params = state.params.mapValues { (param, value) -> value.takeIfValid(param)?.toString() ?: return null }.mapKeys { it.key.name }
		return OrderedProduct.create(product, quantity, params)
	}

	private fun String.takeIfValid(param: Product.Parameter) = when(param.type)
	{
		Product.Parameter.Type.TEXT -> takeIfValidText()
		Product.Parameter.Type.INT -> takeIfValidInt(param)
		Product.Parameter.Type.FLOAT -> takeIfValidFloat(param)
		Product.Parameter.Type.BOOL -> takeIfValidBoolean()
		Product.Parameter.Type.ENUM -> takeIfValidEnum(param)
	}

	private fun String.takeIfValidText() = this

	private fun String.takeIfValidInt(param: Product.Parameter) = takeIfValidNumber(param) { toIntOrNull() }

	private fun String.takeIfValidFloat(param: Product.Parameter) = takeIfValidNumber(param) { toFloatOrNull() }

	private fun <N : Number> String.takeIfValidNumber(param: Product.Parameter, convert: String.() -> N?): N?
	{
		val minValue = param.attributes.minimalValue ?: Float.NEGATIVE_INFINITY
		val maxValue = param.attributes.maximalValue ?: Float.POSITIVE_INFINITY
		val range = minValue..maxValue
		val value = this.convert()
		return value.takeIf { value != null && value.toFloat() in range }
	}

	private fun String.takeIfValidBoolean() = this.toBoolean()

	private fun String.takeIfValidEnum(param: Product.Parameter) =
			this.takeIf { it in param.attributes.enumValues ?: emptyList() }
}

fun RBuilder.productOrderView(product: Product, onOrder: (OrderedProduct) -> Unit, onAddToOrder: (OrderedProduct) -> Unit) =
		child(ProductOrderView::class) {
			attrs.key = product.hashCode().toString() // To handle cases where product's params are changed in meanwhile
			attrs.product = product
			attrs.onOrder = onOrder
			attrs.onAddToOrder = onAddToOrder
		}
