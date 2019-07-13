package pl.karol202.sciorder.client.js.common.view.user

import com.ccfraser.muirwik.components.MColor
import com.ccfraser.muirwik.components.MTypographyVariant
import com.ccfraser.muirwik.components.list.mList
import com.ccfraser.muirwik.components.list.mListItem
import com.ccfraser.muirwik.components.mButton
import com.ccfraser.muirwik.components.mTextField
import com.ccfraser.muirwik.components.mTypography
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
	}

	interface State : RState
	{
		var location: String
		var recipient: String
	}

	private val orderedProducts by prop { orderedProducts }
	private val onDetailsSet by prop { onDetailsSet }

	init
	{
		state.location = ""
		state.recipient = ""
	}

	override fun RBuilder.render()
	{
		flexBox(flexDirection = FlexDirection.column) {
			productsList()
			locationTextField()
			recipientTextField()
			orderButton()
		}
	}

	private fun RBuilder.productsList() = mList {
		orderedProducts.forEach { productItem(it) }
	}

	private fun RBuilder.productItem(orderedProduct: OrderedProduct) = mListItem(button = true) {
		mTypography(text = "${orderedProduct.product.name} x${orderedProduct.quantity}",
		            variant = MTypographyVariant.body2)
	}

	private fun RBuilder.locationTextField() = mTextField(label = "Miejsce dostawy",
	                                                      value = state.location,
	                                                      onChange = { setLocation(it.targetInputValue) }) {
		css {
			specific {
				margin(horizontal = 16.px)
			}
		}
	}

	private fun setLocation(location: String) = setState { this.location = location }

	private fun RBuilder.recipientTextField() = mTextField(label = "Adresat",
	                                                       value = state.recipient,
	                                                       onChange = { setRecipient(it.targetInputValue) }) {
		css {
			specific {
				margin(horizontal = 16.px)
			}
		}
	}

	private fun setRecipient(recipient: String) = setState { this.recipient = recipient }

	private fun RBuilder.orderButton() = mButton(caption = "Zamów",
	                                             color = MColor.secondary,
	                                             onClick = { order() }) {
		css {
			specific {
				margin(16.px)
			}
		}
	}

	private fun order() = onDetailsSet(Order.Details(state.location, state.recipient))
}

fun RBuilder.orderDetailsView(orderedProducts: List<OrderedProduct>,
                              onDetailsSet: (Order.Details) -> Unit) = child(OrderDetailsView::class) {
	attrs.orderedProducts = orderedProducts
	attrs.onDetailsSet = onDetailsSet
}
