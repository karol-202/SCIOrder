package pl.karol202.sciorder.client.js.common.view.admin

import com.ccfraser.muirwik.components.card.mCard
import com.ccfraser.muirwik.components.form.MLabelPlacement
import com.ccfraser.muirwik.components.form.mFormControlLabel
import com.ccfraser.muirwik.components.list.mList
import com.ccfraser.muirwik.components.mCheckbox
import com.ccfraser.muirwik.components.mTextField
import com.ccfraser.muirwik.components.mTypography
import com.ccfraser.muirwik.components.targetInputValue
import kotlinx.css.*
import materialui.icons.iconAdd
import org.w3c.dom.events.Event
import pl.karol202.sciorder.client.js.common.util.*
import pl.karol202.sciorder.client.js.common.view.View
import pl.karol202.sciorder.common.Product
import react.*
import styled.css
import styled.styledDiv

class ProductEditView(props: Props) : View<ProductEditView.Props, ProductEditView.State>(props)
{
	interface Props : RProps
	{
		var product: Product
		var onUpdate: (Product, valid: Boolean) -> Unit
	}
	
	interface State : RState
	
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
		val valid = product.name.isNotBlank()
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
		mTypography(text = "Parametry")
		parametersList()
	}
	
	private fun RBuilder.parametersList() = mList {
		product.parameters.forEach { parameterPanel(it) }
		newParameterPanel()
	}
	
	private fun RBuilder.parameterPanel(param: Product.Parameter) = mCard {
		productParamEditView(param) { newParam, _ -> updateParameter(param, newParam) }
	}
	
	private fun RBuilder.newParameterPanel() = mCard {
		attrs.asDynamic().onClick = { e: Event -> addParameter(Product.Parameter("", Product.Parameter.Type.TEXT, Product.Parameter.Attributes())) }
		iconAdd()
		mTypography(text = "Nowy parametr")
	}
	
	private fun updateName(name: String) = update(product.copy(name = name))
	
	private fun updateAvailability(availability: Boolean) = update(product.copy(available = availability))
	
	private fun updateParameter(oldParam: Product.Parameter, newParam: Product.Parameter) =
			update(product.copy(parameters = product.parameters.replace(oldParam, newParam)))
	
	private fun addParameter(param: Product.Parameter) = update(product.copy(parameters = product.parameters + param))
	
	private fun update(product: Product) = onUpdate(product, product.isValid())
	
	private fun Product.isValid() = name.isNotBlank() && parameters.all { it.isValid() }
	
	private fun Product.Parameter.isValid() = name.isNotBlank()
}

fun RBuilder.productEditView(product: Product,
                             onUpdate: (Product, valid: Boolean) -> Unit) = child(ProductEditView::class) {
	attrs.product = product
	attrs.onUpdate = onUpdate
}
