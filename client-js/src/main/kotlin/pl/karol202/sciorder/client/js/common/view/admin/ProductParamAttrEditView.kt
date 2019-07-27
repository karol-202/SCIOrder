package pl.karol202.sciorder.client.js.common.view.admin

import com.ccfraser.muirwik.components.*
import com.ccfraser.muirwik.components.form.MLabelPlacement
import com.ccfraser.muirwik.components.form.mFormControlLabel
import com.ccfraser.muirwik.components.list.mList
import com.ccfraser.muirwik.components.list.mListItem
import kotlinx.css.*
import materialui.icons.iconAdd
import materialui.icons.iconDelete
import pl.karol202.sciorder.client.js.common.util.*
import pl.karol202.sciorder.client.js.common.view.View
import pl.karol202.sciorder.common.Product
import pl.karol202.sciorder.common.Product.Parameter.Attributes
import pl.karol202.sciorder.common.util.isValidFloat
import react.*
import styled.css
import styled.styledDiv

abstract class ProductParamAttrEditView<S : RState> : View<ProductParamAttrEditView.Props, S>()
{
	interface Props : RProps
	{
		var attrs: Attributes
		var onUpdate: (Attributes) -> Unit
	}
	
	protected val attrs by prop { attrs }
	protected val onUpdate by prop { onUpdate }
	
	class TextView : ProductParamAttrEditView<RState>()
	{
		override fun RBuilder.render()
		{
			defaultValueTextField()
		}
		
		private fun RBuilder.defaultValueTextField() = mTextField(label = "Domyślna wartość",
		                                                          value = attrs.defaultValue ?: "",
		                                                          onChange = { updateDefaultValue(it.targetInputValue) })
	}
	
	class IntView : NumberView(Product.Parameter.Type.INT)
	
	class FloatView : NumberView(Product.Parameter.Type.FLOAT)
	
	abstract class NumberView(private val type: Product.Parameter.Type) : ProductParamAttrEditView<NumberView.State>()
	{
		interface State : RState
		{
			var minimalValue: String
			var maximalValue: String
		}
		
		init
		{
			state.minimalValue = ""
			state.maximalValue = ""
		}
		
		override fun RBuilder.render()
		{
			defaultValueTextField()
			rangePanel()
		}
		
		private fun RBuilder.defaultValueTextField(): ReactElement
		{
			val valid = attrs.isDefaultValueValidFor(type)
			return mTextField(label = "Domyślna wartość",
			                  helperText = if(!valid) "Niewłaściwa wartość" else "",
			                  error = !valid,
			                  value = attrs.defaultValue ?: "",
			                  onChange = { updateDefaultValue(it.targetInputValue) })
		}
		
		private fun RBuilder.rangePanel() = styledDiv {
			cssFlexBox(direction = FlexDirection.row,
			           alignItems = Align.flexStart)
			
			minimalValueTextField()
			rangeSeparator()
			maximalValueTextField()
		}
		
		private fun RBuilder.minimalValueTextField(): ReactElement
		{
			val valid = attrs.isMinimalValueValidFor(type)
			return mTextField(label = "Minimalna wartość",
			                  helperText = if(!valid) "Niewłaściwa wartość" else "",
			                  error = !valid,
			                  value = state.minimalValue,
			                  onChange = { updateMinimalValue(it.targetInputValue) }) {
				cssFlexItem(grow = 1.0)
			}
		}
		
		private fun RBuilder.rangeSeparator() = mTypography(text = "-") {
			overrideCss { margin(top = 36.px, left = 16.px, right = 16.px) }
		}
		
		private fun RBuilder.maximalValueTextField(): ReactElement
		{
			val valid = attrs.isMaximalValueValidFor(type)
			return mTextField(label = "Maksymalna wartość",
			                  helperText = if(!valid) "Niewłaściwa wartość" else "",
			                  error = !valid,
			                  value = state.maximalValue,
			                  onChange = { updateMaximalValue(it.targetInputValue) }) {
				cssFlexItem(grow = 1.0)
			}
		}
		
		private fun updateMinimalValue(minimalValue: String)
		{
			setState { this.minimalValue = minimalValue }
			updateMinimalValue(when
			                   {
				                   minimalValue.isEmpty() -> null
				                   minimalValue.isValidFloat() -> minimalValue.toFloatOrNull()
				                   else -> kotlin.Float.POSITIVE_INFINITY
			                   })
		}
		
		private fun updateMaximalValue(maximalValue: String)
		{
			setState { this.maximalValue = maximalValue }
			updateMaximalValue(when
			                   {
				                   maximalValue.isEmpty() -> null
				                   maximalValue.isValidFloat() -> maximalValue.toFloatOrNull()
				                   else -> kotlin.Float.NEGATIVE_INFINITY
			                   })
		}
	}
	
	class BooleanView : ProductParamAttrEditView<RState>()
	{
		override fun RBuilder.render()
		{
			defaultValuePanel()
		}
		
		private fun RBuilder.defaultValuePanel() = mFormControlLabel(label = "Domyślna wartość",
		                                                             labelPlacement = MLabelPlacement.start,
		                                                             control = createDefaultValueCheckbox()) {
			cssFlexItem(alignSelf = Align.flexStart)
			overrideCss { margin(left = 0.px) }
		}
		
		private fun createDefaultValueCheckbox() = buildElement {
			mCheckbox(primary = false,
			          checked = attrs.defaultValue?.toBoolean() ?: false,
			          onChange = { _, checked -> updateDefaultValue(checked.toString()) })
		}!!
	}
	
	class EnumView : ProductParamAttrEditView<RState>()
	{
		private val enumValues get() = attrs.enumValues ?: emptyList()
		
		override fun RBuilder.render()
		{
			itemsList()
		}
		
		private fun RBuilder.itemsList() = mList {
			enumValues.forEachIndexed { id, value -> item(id, value) }
			newItem()
		}
		
		private fun RBuilder.item(id: Int, value: String) = mListItem {
			key = id.toString()
			defaultRadio(value)
			valueTextField(value) { updateValue(id, it) }
			deleteButton { deleteValue(id, value) }
		}
		
		private fun RBuilder.newItem() = mListItem {
			key = enumValues.size.toString()
			styledDiv {
				css { margin(9.px) }
				iconAdd()
			}
			valueTextField("") { addValue(it) }
		}
		
		private fun RBuilder.defaultRadio(value: String) = mRadio(primary = false,
		                                                          checked = value.isDefaultValue(),
		                                                          onChange = { _, checked ->
			                                                          if(checked) updateDefaultValue(value)
		                                                          })
		
		private fun RBuilder.valueTextField(value: String, onChange: (String) -> Unit): ReactElement
		{
			val valid = attrs.areEnumValuesValidFor(Product.Parameter.Type.ENUM)
			return mTextField(label = "",
			                  helperText = if(!valid) "Podaj wartość" else "",
			                  error = !valid,
			                  value = value,
			                  onChange = { onChange(it.targetInputValue) }) {
				cssFlexItem(grow = 1.0)
			}
		}
		
		private fun RBuilder.deleteButton(onDelete: () -> Unit) =
				mIconButton(onClick = { onDelete() }) {
					overrideCss { marginLeft = 12.px }
					iconDelete()
				}
		
		private fun String.isDefaultValue() = this == attrs.defaultValue
		
		private fun addValue(value: String) = updateEnumValues(enumValues + value)
		
		private fun updateValue(id: Int, value: String) =
				updateEnumValues(enumValues.replaceIndex(id, value))
		
		private fun deleteValue(id: Int, value: String)
		{
			val enumValues = enumValues.removeIndex(id)
			val wasTheOnlyOne = enumValues.count { it == value } <= 1 // Rerenders may be batched!
			if(wasTheOnlyOne) updateEnumValues(enumValues, null)
			else updateEnumValues(enumValues)
		}
	}
	
	protected fun updateDefaultValue(defaultValue: String?) = update(attrs.copy(defaultValue = defaultValue))
	
	protected fun updateMinimalValue(minimalValue: kotlin.Float?) = update(attrs.copy(minimalValue = minimalValue))
	
	protected fun updateMaximalValue(maximalValue: kotlin.Float?) = update(attrs.copy(maximalValue = maximalValue))
	
	protected fun updateEnumValues(enumValues: List<String>?) = update(attrs.copy(enumValues = enumValues))
	
	protected fun updateEnumValues(enumValues: List<String>?, defaultValue: String?) =
			update(attrs.copy(enumValues = enumValues, defaultValue = defaultValue))
	
	private fun update(attrs: Attributes) = onUpdate(attrs)
}

fun RBuilder.productParamAttrTextEditView(attrs: Attributes,
                                          onUpdate: (Attributes) -> Unit) =
		productParamAttrEditView<ProductParamAttrEditView.TextView>(attrs, onUpdate)

fun RBuilder.productParamAttrIntEditView(attrs: Attributes,
                                         onUpdate: (Attributes) -> Unit) =
		productParamAttrEditView<ProductParamAttrEditView.IntView>(attrs, onUpdate)

fun RBuilder.productParamAttrFloatEditView(attrs: Attributes,
                                           onUpdate: (Attributes) -> Unit) =
		productParamAttrEditView<ProductParamAttrEditView.FloatView>(attrs, onUpdate)

fun RBuilder.productParamAttrBooleanEditView(attrs: Attributes,
                                             onUpdate: (Attributes) -> Unit) =
		productParamAttrEditView<ProductParamAttrEditView.BooleanView>(attrs, onUpdate)

fun RBuilder.productParamAttrEnumEditView(attrs: Attributes,
                                          onUpdate: (Attributes) -> Unit) =
		productParamAttrEditView<ProductParamAttrEditView.EnumView>(attrs, onUpdate)

private inline fun <reified V : ProductParamAttrEditView<*>> RBuilder.productParamAttrEditView(
		attrs: Attributes,
		noinline onUpdate: (Attributes) -> Unit
) = child(V::class) {
	this.attrs.attrs = attrs
	this.attrs.onUpdate = onUpdate
}
