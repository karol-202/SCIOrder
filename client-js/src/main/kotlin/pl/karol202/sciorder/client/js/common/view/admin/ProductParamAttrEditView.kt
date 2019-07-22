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
import pl.karol202.sciorder.client.js.common.util.removeIndex
import pl.karol202.sciorder.client.js.common.util.replaceIndex
import pl.karol202.sciorder.client.js.common.view.View
import pl.karol202.sciorder.common.Product
import pl.karol202.sciorder.common.Product.Parameter.Attributes
import react.*

abstract class ProductParamAttrEditView : View<ProductParamAttrEditView.Props, RState>()
{
	interface Props : RProps
	{
		var attrs: Attributes
		var onUpdate: (Attributes) -> Unit
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
	
	abstract class NumberView(private val type: Product.Parameter.Type) : ProductParamAttrEditView()
	{
		override fun RBuilder.render()
		{
			defaultValueTextField()
			minimalValueTextField()
			maximalValueTextField()
		}
		
		private fun RBuilder.defaultValueTextField(): ReactElement
		{
			val valid = attrs.isDefaultValueValidFor(type)
			return mTextField(label = "Domyślna wartość",
			                  helperText = if(!valid) "Niewłaściwa wartość" else "",
			                  error = !valid,
			                  value = attrs.defaultValue,
			                  onChange = { updateDefaultValue(it.targetInputValue) })
		}
		
		private fun RBuilder.minimalValueTextField(): ReactElement
		{
			val valid = attrs.isMinimalValueValidFor(type)
			return mTextField(label = "Minimalna wartość",
			                  helperText = if(!valid) "Niewłaściwa wartość" else "",
			                  error = !valid,
			                  value = attrs.minimalValue?.toString(),
			                  onChange = { updateMinimalValue(it.targetInputValue.toFloatOrNull()) })
		}
		
		private fun RBuilder.maximalValueTextField(): ReactElement
		{
			val valid = attrs.isMaximalValueValidFor(type)
			return mTextField(label = "Maksymalna wartość",
			                  helperText = if(!valid) "Niewłaściwa wartość" else "",
			                  error = !valid,
			                  value = attrs.minimalValue?.toString(),
			                  onChange = { updateMaximalValue(it.targetInputValue.toFloatOrNull()) })
		}
	}
	
	class IntView : NumberView(Product.Parameter.Type.INT)
	
	class FloatView : NumberView(Product.Parameter.Type.FLOAT)
	
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
				mIconButton(onClick = { onDelete() }) { iconDelete() }
		
		private fun String.isDefaultValue() = this == attrs.defaultValue
		
		private fun addValue(value: String) = updateEnumValues(enumValues + value)
		
		private fun updateValue(id: Int, value: String) =
				updateEnumValues(enumValues.replaceIndex(id, value))
		
		private fun deleteValue(id: Int, value: String)
		{
			updateEnumValues(enumValues.removeIndex(id))
			val wasTheOnlyOne = enumValues.none { it == value }
			if(wasTheOnlyOne) updateDefaultValue(null)
		}
	}
	
	protected fun updateDefaultValue(defaultValue: String?) = update(attrs.copy(defaultValue = defaultValue))
	
	protected fun updateMinimalValue(minimalValue: Float?) = update(attrs.copy(minimalValue = minimalValue))
	
	protected fun updateMaximalValue(maximalValue: Float?) = update(attrs.copy(maximalValue = maximalValue))
	
	protected fun updateEnumValues(enumValues: List<String>?) = update(attrs.copy(enumValues = enumValues))
	
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

private inline fun <reified V : ProductParamAttrEditView> RBuilder.productParamAttrEditView(
		attrs: Attributes,
		noinline onUpdate: (Attributes) -> Unit
) = child(V::class) {
	this.attrs.attrs = attrs
	this.attrs.onUpdate = onUpdate
}
