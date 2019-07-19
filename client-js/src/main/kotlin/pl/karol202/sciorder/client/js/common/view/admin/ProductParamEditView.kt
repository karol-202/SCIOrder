package pl.karol202.sciorder.client.js.common.view.admin

import com.ccfraser.muirwik.components.mMenuItem
import com.ccfraser.muirwik.components.mTextField
import com.ccfraser.muirwik.components.mTextFieldSelect
import com.ccfraser.muirwik.components.targetInputValue
import kotlinx.css.Align
import kotlinx.css.FlexDirection
import kotlinx.css.pct
import kotlinx.css.width
import pl.karol202.sciorder.client.js.common.model.visibleName
import pl.karol202.sciorder.client.js.common.util.cssFlexBox
import pl.karol202.sciorder.client.js.common.util.prop
import pl.karol202.sciorder.client.js.common.view.View
import pl.karol202.sciorder.common.Product
import react.*
import styled.css
import styled.styledDiv

class ProductParamEditView : View<ProductParamEditView.Props, ProductParamEditView.State>()
{
	interface Props : RProps
	{
		var parameter: Product.Parameter
		var onUpdate: (Product.Parameter, valid: Boolean) -> Unit
	}
	
	interface State : RState
	{
		var attributesValidity: Boolean
	}
	
	private val parameter by prop { parameter }
	private val onUpdate by prop { onUpdate }
	
	init
	{
		state.attributesValidity = true
	}
	
	override fun RBuilder.render()
	{
		styledDiv {
			cssFlexBox(direction = FlexDirection.column,
			           alignItems = Align.stretch)
			css { width = 100.pct }
			
			nameTextField()
			typeSelect()
			attributesView()
		}
	}
	
	private fun RBuilder.nameTextField(): ReactElement
	{
		val valid = parameter.name.isNotBlank()
		return mTextField(label = "Nazwa parametru",
		                  helperText = if(!valid) "Podaj nazwę" else null,
		                  error = !valid,
		                  value = parameter.name,
		                  onChange = { updateName(it.targetInputValue) })
	}
	
	private fun RBuilder.typeSelect() = mTextFieldSelect(label = "Typ",
	                                                     value = parameter.type.name,
	                                                     onChange = { updateType(it.target.toString()) }) {
		Product.Parameter.Type.values().forEach { typeItem(it) }
	}
	
	private fun RBuilder.typeItem(type: Product.Parameter.Type) = mMenuItem(primaryText = type.visibleName,
	                                                                        value = type.name)
	
	private fun RBuilder.attributesView() = when(parameter.type)
	{
		Product.Parameter.Type.TEXT -> ::productParamAttrTextEditView
		Product.Parameter.Type.INT -> ::productParamAttrIntEditView
		Product.Parameter.Type.FLOAT -> ::productParamAttrFloatEditView
		Product.Parameter.Type.BOOL -> ::productParamAttrBooleanEditView
		Product.Parameter.Type.ENUM -> ::productParamAttrEnumEditView
	}(parameter.attributes) { attrs, valid -> updateAttrs(attrs, valid) }
	
	private fun updateName(name: String) = update(parameter.copy(name = name))
	
	private fun updateType(typeName: String) =
			Product.Parameter.Type.getByName(typeName)?.let { update(parameter.copy(type = it)) }
	
	private fun updateAttrs(attrs: Product.Parameter.Attributes, valid: Boolean)
	{
		update(parameter.copy(attributes = attrs))
		setState { attributesValidity = valid }
	}
	
	private fun update(param: Product.Parameter) = onUpdate(param, param.isValid())
	
	private fun Product.Parameter.isValid() = name.isNotBlank()
}

fun RBuilder.productParamEditView(parameter: Product.Parameter,
                                  onUpdate: (Product.Parameter, valid: Boolean) -> Unit) = child(ProductParamEditView::class) {
	attrs.parameter = parameter
	attrs.onUpdate = onUpdate
}
