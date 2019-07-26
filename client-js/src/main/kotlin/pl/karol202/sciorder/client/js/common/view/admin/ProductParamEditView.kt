package pl.karol202.sciorder.client.js.common.view.admin

import com.ccfraser.muirwik.components.*
import kotlinx.css.*
import materialui.icons.iconDelete
import pl.karol202.sciorder.client.js.common.model.visibleName
import pl.karol202.sciorder.client.js.common.util.cssFlexBox
import pl.karol202.sciorder.client.js.common.util.cssFlexItem
import pl.karol202.sciorder.client.js.common.util.overrideCss
import pl.karol202.sciorder.client.js.common.util.prop
import pl.karol202.sciorder.client.js.common.view.View
import pl.karol202.sciorder.common.Product
import react.RBuilder
import react.RProps
import react.RState
import react.ReactElement
import styled.css
import styled.styledDiv

class ProductParamEditView : View<ProductParamEditView.Props, RState>()
{
	interface Props : RProps
	{
		var parameter: Product.Parameter
		var nameDuplicated: Boolean
		var onUpdate: (Product.Parameter) -> Unit
		var onDelete: () -> Unit
	}
	
	private val parameter by prop { parameter }
	private val nameDuplicated by prop { nameDuplicated }
	private val onUpdate by prop { onUpdate }
	private val onDelete by prop { onDelete }
	
	override fun RBuilder.render()
	{
		styledDiv {
			cssFlexBox(direction = FlexDirection.column,
			           alignItems = Align.stretch)
			css {
				width = 100.pct
				padding(bottom = 16.px, left = 24.px, right = 24.px)
			}
			
			headerPanel()
			typeSelect()
			attributesView()
		}
	}
	
	private fun RBuilder.headerPanel() = styledDiv {
		cssFlexBox(direction = FlexDirection.row,
		           alignItems = Align.flexStart)
		
		nameTextField()
		deleteButton()
	}
	
	private fun RBuilder.nameTextField(): ReactElement
	{
		val error = when
		{
			!parameter.isNameValid -> "Podaj nazwę"
			nameDuplicated -> "Nazwy muszą się różnić"
			else -> null
		}
		return mTextField(label = "Nazwa parametru",
		                  helperText = error,
		                  error = error != null,
		                  value = parameter.name,
		                  onChange = { updateName(it.targetInputValue) }) {
			cssFlexItem(grow = 1.0)
		}
	}
	
	private fun RBuilder.deleteButton() = mIconButton(onClick = { onDelete() }) {
		overrideCss { margin(left = 12.px, top = 20.px) }
		iconDelete()
	}
	
	private fun RBuilder.typeSelect() = mTextFieldSelect(label = "Typ",
	                                                     value = parameter.type.name,
	                                                     onChange = { updateType(it.targetValue.toString()) }) {
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
	}(parameter.attributes) { updateAttrs(it) }
	
	private fun updateName(name: String) = update(parameter.copy(name = name))
	
	private fun updateType(typeName: String) =
			Product.Parameter.Type.getByName(typeName)?.let { update(parameter.copy(type = it)) }
	
	private fun updateAttrs(attrs: Product.Parameter.Attributes) = update(parameter.copy(attributes = attrs))
	
	private fun update(param: Product.Parameter) = onUpdate(param)
}

fun RBuilder.productParamEditView(parameter: Product.Parameter,
                                  nameDuplicated: Boolean,
                                  onUpdate: (Product.Parameter) -> Unit,
                                  onDelete: () -> Unit) = child(ProductParamEditView::class) {
	attrs.parameter = parameter
	attrs.nameDuplicated = nameDuplicated
	attrs.onUpdate = onUpdate
	attrs.onDelete = onDelete
}
