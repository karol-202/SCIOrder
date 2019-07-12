package pl.karol202.sciorder.client.js.common.view

import com.ccfraser.muirwik.components.MButtonVariant
import com.ccfraser.muirwik.components.MColor
import com.ccfraser.muirwik.components.MTypographyVariant
import com.ccfraser.muirwik.components.mButton
import com.ccfraser.muirwik.components.mTextField
import com.ccfraser.muirwik.components.mTypography
import com.ccfraser.muirwik.components.targetInputValue
import kotlinx.css.Align
import kotlinx.css.FlexDirection
import kotlinx.css.marginBottom
import kotlinx.css.px
import pl.karol202.sciorder.client.js.common.util.flexBox
import pl.karol202.sciorder.client.js.common.util.onEnterEventListener
import pl.karol202.sciorder.client.js.common.util.prop
import pl.karol202.sciorder.client.js.common.viewmodel.OwnerJsViewModel
import react.RBuilder
import react.RProps
import react.RState
import react.setState
import styled.css
import styled.styledDiv

class UserLoginView(props: Props) : View<UserLoginView.Props, UserLoginView.State>(props)
{
	interface Props : RProps
	{
		var ownerViewModel: OwnerJsViewModel?
	}

	interface State : RState
	{
		var ownerName: String
	}

	private val ownerViewModel by prop { ownerViewModel }

	init
	{
		state.ownerName = ""
	}

	override fun RBuilder.render()
	{
		flexBox(flexDirection = FlexDirection.column,
		        alignItems = Align.stretch) {
			titleText()
			ownerTextField()
			loginButton()
		}
	}

	private fun RBuilder.titleText() = mTypography("Zaloguj się", variant = MTypographyVariant.h5)

	private fun RBuilder.ownerTextField() = styledDiv {
		css {
			marginBottom = 16.px
		}
		mTextField("Konto",
		           fullWidth = true,
		           value = state.ownerName,
		           onChange = { onOwnerNameChange(it.targetInputValue) }) {
			onEnterEventListener { login() }
		}
	}

	private fun RBuilder.loginButton() = mButton("Zaloguj",
	                                             color = MColor.secondary,
	                                             variant = MButtonVariant.contained,
	                                             fullWidth = true,
	                                             onClick = { login() })

	private fun onOwnerNameChange(newName: String) = setState { ownerName = newName }

	private fun login()
	{
		ownerViewModel.login(state.ownerName)
	}
}

fun RBuilder.userLoginView(ownerViewModel: OwnerJsViewModel) = child(UserLoginView::class) {
	attrs.ownerViewModel = ownerViewModel
}
