package pl.karol202.sciorder.client.js.common.view.user

import com.ccfraser.muirwik.components.MColor
import com.ccfraser.muirwik.components.mButton
import com.ccfraser.muirwik.components.mTextField
import com.ccfraser.muirwik.components.targetInputValue
import kotlinx.css.FlexDirection
import kotlinx.css.margin
import kotlinx.css.px
import pl.karol202.sciorder.client.common.model.OrderedProduct
import pl.karol202.sciorder.client.js.common.util.flexBox
import pl.karol202.sciorder.client.js.common.util.prop
import pl.karol202.sciorder.client.js.common.view.View
import pl.karol202.sciorder.common.Order
import react.RBuilder
import react.RProps
import react.RState
import react.setState
import styled.css

class OrderDetailsView : View<OrderDetailsView.Props, OrderDetailsView.State>()
{
	interface Props : RProps
	{
		var orderedProducts: List<OrderedProduct>
		var onDetailsSet: (Order.Details) -> Unit
		var onCancel: () -> Unit
	}

	interface State : RState
	{
		var location: String
		var recipient: String
	}

	private val orderedProducts by prop { orderedProducts }
	private val onDetailsSet by prop { onDetailsSet }
	private val onCancel by prop { onCancel }

	init
	{
		state.location = ""
		state.recipient = ""
	}

	override fun RBuilder.render()
	{
		flexBox(direction = FlexDirection.column) {
			productsList()
			locationTextField()
			recipientTextField()
			
			flexBox(direction = FlexDirection.rowReverse) {
				orderButton()
				cancelButton()
			}
		}
	}

	private fun RBuilder.productsList() = orderedProductsView(orderedProducts = orderedProducts,
	                                                          horizontalPadding = 24.px)

	private fun RBuilder.locationTextField() = mTextField(label = "Miejsce dostawy",
	                                                      value = state.location,
	                                                      onChange = { setLocation(it.targetInputValue) }) {
		css { specific { margin(horizontal = 24.px) } }
	}

	private fun setLocation(location: String) = setState { this.location = location }

	private fun RBuilder.recipientTextField() = mTextField(label = "Adresat",
	                                                       value = state.recipient,
	                                                       onChange = { setRecipient(it.targetInputValue) }) {
		css { specific { margin(horizontal = 24.px) } }
	}

	private fun setRecipient(recipient: String) = setState { this.recipient = recipient }
	
	private fun RBuilder.orderButton() = mButton(caption = "Zamów",
	                                             color = MColor.secondary,
	                                             onClick = { order() }) {
		css { specific { margin(vertical = 16.px, horizontal = 24.px) } }
	}

	private fun order() = onDetailsSet(Order.Details(state.location, state.recipient))
	
	private fun RBuilder.cancelButton() = mButton(caption = "Anuluj",
	                                              color = MColor.secondary,
	                                              onClick = { onCancel() }) {
		css { specific { margin(vertical = 16.px) } }
	}
}

fun RBuilder.orderDetailsView(orderedProducts: List<OrderedProduct>,
                              onDetailsSet: (Order.Details) -> Unit,
                              onCancel: () -> Unit) = child(OrderDetailsView::class) {
	attrs.orderedProducts = orderedProducts
	attrs.onDetailsSet = onDetailsSet
	attrs.onCancel = onCancel
}
