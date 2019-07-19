package pl.karol202.sciorder.client.js.common.view.admin

import com.ccfraser.muirwik.components.*
import com.ccfraser.muirwik.components.form.MLabelPlacement
import com.ccfraser.muirwik.components.form.mFormControlLabel
import com.ccfraser.muirwik.components.list.mList
import com.ccfraser.muirwik.components.list.mListItem
import materialui.icons.iconAdd
import materialui.icons.iconDelete
import pl.karol202.sciorder.client.js.common.util.cssFlexItem
import pl.karol202.sciorder.client.js.common.util.prop
import pl.karol202.sciorder.client.js.common.view.View
import pl.karol202.sciorder.common.Product.Parameter.Attributes
import react.*

abstract class ProductParamAttrEditView : View<ProductParamAttrEditView.Props, RState>()
{
	interface Props : RProps
	{
		var attrs: Attributes
		var onUpdate: (Attributes, valid: Boolean) -> Unit
	}
	
	protected val attrs by prop { attrs }
	protected val onUpdate by prop { onUpdate }
	
	class TextView : ProductParamAttrEditView()
	{
		override fun RBuilder.render()
		{
			defaultValueTextField()
		}
		
		private fun RBuilder.defaultValueTextField() = mTextField(label = "Domyślna wartość",
		                                                          value = attrs.defaultValue,
		                                                          onChange = { updateDefaultValue(it.targetInputValue) })
	}
	
	abstract class NumberView : ProductParamAttrEditView()
	{
		override fun RBuilder.render()
		{
			defaultValueTextField()
			minimalValueTextField()
			maximalValueTextField()
		}
		
		private fun RBuilder.defaultValueTextField(): ReactElement
		{
			val valid = attrs.defaultValue.isValidDefaultValue()
			return mTextField(label = "Domyślna wartość",
			                  helperText = if(!valid) "Niewłaściwa wartość" else "",
			                  error = !valid,
			                  value = attrs.defaultValue,
			                  onChange = { updateDefaultValue(it.targetInputValue) })
		}
		
		private fun RBuilder.minimalValueTextField(): ReactElement
		{
			val valid = attrs.minimalValue.isValidMinimalValue()
			return mTextField(label = "Minimalna wartość",
			                  helperText = if(!valid) "Niewłaściwa wartość" else "",
			                  error = !valid,
			                  value = attrs.minimalValue?.toString(),
			                  onChange = { updateMinimalValue(it.targetInputValue.toNumberOrNull()?.toFloat()) })
		}
		
		private fun RBuilder.maximalValueTextField(): ReactElement
		{
			val valid = attrs.maximalValue.isValidMaximalValue()
			return mTextField(label = "Maksymalna wartość",
			                  helperText = if(!valid) "Niewłaściwa wartość" else "",
			                  error = !valid,
			                  value = attrs.minimalValue?.toString(),
			                  onChange = { updateMaximalValue(it.targetInputValue.toNumberOrNull()?.toFloat()) })
		}
		
		override fun Attributes.isValid() =
				defaultValue.isValidDefaultValue() && minimalValue.isValidMinimalValue() && maximalValue.isValidMaximalValue()
		
		private fun String?.isValidDefaultValue() = this == null || (isValidNumber() && toNumberOrNull()!!.toFloat().inRange())
		
		private fun Float?.isValidMinimalValue() = this == null || lesserThanMax()
		
		private fun Float?.isValidMaximalValue() = this == null || greaterThanMin()
		
		private fun String.isValidNumber() = toNumberOrNull() != null
		
		private fun Float.inRange(): Boolean
		{
			val minValue = attrs.minimalValue ?: Float.MIN_VALUE
			val maxValue = attrs.maximalValue ?: Float.MAX_VALUE
			val range = minValue..maxValue
			return this in range
		}
		
		private fun Float.lesserThanMax() = this < attrs.maximalValue ?: Float.MAX_VALUE
		
		private fun Float.greaterThanMin() = this > attrs.minimalValue ?: Float.MIN_VALUE
		
		protected abstract fun String.toNumberOrNull(): Number?
	}
	
	class IntView : NumberView()
	{
		override fun String.toNumberOrNull() = toIntOrNull()
	}
	
	class FloatView : NumberView()
	{
		override fun String.toNumberOrNull() = toFloatOrNull()
	}
	
	class BooleanView : ProductParamAttrEditView()
	{
		override fun RBuilder.render()
		{
			defaultValuePanel()
		}
		
		private fun RBuilder.defaultValuePanel() = mFormControlLabel(label = "Domyślna wartość",
		                                                             labelPlacement = MLabelPlacement.start,
		                                                             control = createDefaultValueCheckbox())
		
		private fun createDefaultValueCheckbox() = buildElement {
			mCheckbox(primary = false,
			          checked = attrs.defaultValue?.toBoolean() ?: false,
			          onChange = { _, checked -> updateDefaultValue(checked.toString()) })
		}!!
	}
	
	class EnumView : ProductParamAttrEditView()
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
		
		private fun RBuilder.item(id: Int, value: String) = mListItem(button = true) {
			defaultRadio(value)
			valueTextField(value) { updateValue(id, it) }
			deleteButton { deleteValue(id, value) }
		}
		
		private fun RBuilder.newItem() = mListItem(button = true) {
			iconAdd()
			valueTextField("") { addValue(it) }
		}
		
		private fun RBuilder.defaultRadio(value: String) = mRadio(primary = false,
		                                                          checked = value.isDefaultValue(),
		                                                          onChange = { _, checked ->
			                                                          if(checked) updateDefaultValue(value)
		                                                          })
		
		private fun RBuilder.valueTextField(value: String, onChange: (String) -> Unit): ReactElement
		{
			val valid = enumValues.areValidEnumValues()
			return mTextField(label = "",
			                  helperText = if(!valid) "Podaj wartość" else "",
			                  error = !valid,
			                  value = value,
			                  onChange = { onChange(it.targetInputValue) }) {
				cssFlexItem(grow = 1.0)
			}
		}
		
		private fun RBuilder.deleteButton(onDelete: () -> Unit) =
				mIconButton(onClick = { onDelete() }) { iconDelete() }
		
		private fun String.isDefaultValue() = this == attrs.defaultValue
		
		private fun addValue(value: String) = updateEnumValues(enumValues + value)
		
		private fun updateValue(id: Int, value: String) =
				updateEnumValues(enumValues.mapIndexed { i, v -> if(i == id) value else v })
		
		private fun deleteValue(id: Int, value: String)
		{
			updateEnumValues(enumValues.mapIndexedNotNull { i, v -> if(i != id) v else null })
			val wasTheOnlyOne = enumValues.none { it == value }
			if(wasTheOnlyOne) updateDefaultValue(null)
		}
		
		override fun Attributes.isValid() = enumValues.areValidEnumValues()
		
		private fun List<String>?.areValidEnumValues() = this != null && this.isNotEmpty()
	}
	
	protected fun updateDefaultValue(defaultValue: String?) = update(attrs.copy(defaultValue = defaultValue))
	
	protected fun updateMinimalValue(minimalValue: Float?) = update(attrs.copy(minimalValue = minimalValue))
	
	protected fun updateMaximalValue(maximalValue: Float?) = update(attrs.copy(maximalValue = maximalValue))
	
	protected fun updateEnumValues(enumValues: List<String>?) = update(attrs.copy(enumValues = enumValues))
	
	private fun update(attrs: Attributes) = onUpdate(attrs, attrs.isValid())
	
	protected open fun Attributes.isValid() = true
}

fun RBuilder.productParamAttrTextEditView(attrs: Attributes,
                                          onUpdate: (Attributes, valid: Boolean) -> Unit) =
		productParamAttrEditView<ProductParamAttrEditView.TextView>(attrs, onUpdate)

fun RBuilder.productParamAttrIntEditView(attrs: Attributes,
                                         onUpdate: (Attributes, valid: Boolean) -> Unit) =
		productParamAttrEditView<ProductParamAttrEditView.IntView>(attrs, onUpdate)

fun RBuilder.productParamAttrFloatEditView(attrs: Attributes,
                                           onUpdate: (Attributes, valid: Boolean) -> Unit) =
		productParamAttrEditView<ProductParamAttrEditView.FloatView>(attrs, onUpdate)

fun RBuilder.productParamAttrBooleanEditView(attrs: Attributes,
                                             onUpdate: (Attributes, valid: Boolean) -> Unit) =
		productParamAttrEditView<ProductParamAttrEditView.BooleanView>(attrs, onUpdate)

fun RBuilder.productParamAttrEnumEditView(attrs: Attributes,
                                          onUpdate: (Attributes, valid: Boolean) -> Unit) =
		productParamAttrEditView<ProductParamAttrEditView.EnumView>(attrs, onUpdate)

private inline fun <reified V : ProductParamAttrEditView> RBuilder.productParamAttrEditView(
		attrs: Attributes,
		noinline onUpdate: (Attributes, valid: Boolean) -> Unit
) = child(V::class) {
	this.attrs.attrs = attrs
	this.attrs.onUpdate = onUpdate
}
