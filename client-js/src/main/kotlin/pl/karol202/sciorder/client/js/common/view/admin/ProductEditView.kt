package pl.karol202.sciorder.client.js.common.view.admin

import com.ccfraser.muirwik.components.*
import com.ccfraser.muirwik.components.card.mCard
import com.ccfraser.muirwik.components.form.MLabelPlacement
import com.ccfraser.muirwik.components.form.mFormControlLabel
import com.ccfraser.muirwik.components.list.mList
import com.ccfraser.muirwik.components.list.mListItem
import kotlinx.css.*
import materialui.icons.iconAdd
import pl.karol202.sciorder.client.common.model.NEW_PARAMETER
import pl.karol202.sciorder.client.js.common.util.*
import pl.karol202.sciorder.client.js.common.view.View
import pl.karol202.sciorder.common.Product
import pl.karol202.sciorder.common.duplicatedParameterNames
import react.*
import styled.css
import styled.styledDiv

class ProductEditView(props: Props) : View<ProductEditView.Props, RState>(props)
{
	interface Props : RProps
	{
		var product: Product
		var onUpdate: (Product) -> Unit
	}
	
	private val product by prop { product }
	private val onUpdate by prop { onUpdate }
	
	override fun RBuilder.render()
	{
		styledDiv {
			cssFlexBox(direction = FlexDirection.column,
			           alignItems = Align.stretch)
			css { width = 100.pct }
			
			nameTextField()
			availabilityPanel()
			parametersPanel()
		}
	}
	
	private fun RBuilder.nameTextField(): ReactElement
	{
		val valid = product.isNameValid
		return mTextField(label = "Nazwa",
		                  helperText = if(!valid) "Podaj nazwę" else null,
		                  error = !valid,
		                  value = product.name,
		                  onChange = { updateName(it.targetInputValue) }) {
			overrideCss { marginTop = 0.px }
		}
	}
	
	private fun RBuilder.availabilityPanel() = mFormControlLabel(label = "Dostępny",
	                                                             labelPlacement = MLabelPlacement.start,
	                                                             control = createAvailabilityCheckbox()) {
		cssFlexItem(alignSelf = Align.flexStart)
		overrideCss { margin(left = 0.px, top = 8.px) }
	}
	
	private fun createAvailabilityCheckbox() = buildElement {
		mCheckbox(primary = false,
		          checked = product.available,
		          onChange = { _, checked -> updateAvailability(checked) })
	}!!
	
	private fun RBuilder.parametersPanel() = styledDiv {
		css { marginTop = 8.px }
		
		mTypography(text = "Parametry")
		parametersList()
	}
	
	private fun RBuilder.parametersList() = mList {
		product.parameters.forEachIndexed { i, param -> parameterPanel(i, param) }
		newParameterPanel()
	}
	
	private fun RBuilder.parameterPanel(index: Int, param: Product.Parameter) = mCard {
		overrideCss {
			marginBottom = 24.px
			backgroundColor = Colors.Grey.shade700
		}
		
		productParamEditView(parameter = param,
		                     nameDuplicated = param.name in product.parameters.duplicatedParameterNames,
		                     onUpdate = { updateParameter(index, it) },
		                     onDelete = { deleteParameter(param) })
	}
	
	private fun RBuilder.newParameterPanel() = mCard {
		overrideCss { backgroundColor = Colors.Grey.shade700 }
		
		mListItem(button = true,
		          disableGutters = true,
		          onClick = { addParameter(Product.Parameter.NEW_PARAMETER) }) {
			styledDiv {
				css { margin(horizontal = 16.px, vertical = 4.px) }
				iconAdd()
			}
			mTypography(text = "Nowy parametr")
		}
	}
	
	private fun updateName(name: String) = update(product.copy(name = name))
	
	private fun updateAvailability(availability: Boolean) = update(product.copy(available = availability))
	
	private fun updateParameters(parameters: List<Product.Parameter>) = update(product.copy(parameters = parameters))
	
	private fun addParameter(param: Product.Parameter) = updateParameters(product.parameters + param)
	
	private fun updateParameter(index: Int, param: Product.Parameter) =
			updateParameters(product.parameters.replaceIndex(index, param))
	
	private fun deleteParameter(param: Product.Parameter) = updateParameters(product.parameters - param)
	
	private fun update(product: Product) = onUpdate(product)
}

fun RBuilder.productEditView(product: Product,
                             onUpdate: (Product) -> Unit) = child(ProductEditView::class) {
	attrs.product = product
	attrs.onUpdate = onUpdate
}
