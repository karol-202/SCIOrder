package pl.karol202.sciorder.client.js.common.view.admin

import com.ccfraser.muirwik.components.MButtonVariant
import com.ccfraser.muirwik.components.MColor
import com.ccfraser.muirwik.components.MTypographyVariant
import com.ccfraser.muirwik.components.form.mFormControl
import com.ccfraser.muirwik.components.input.MInputAdornmentPosition
import com.ccfraser.muirwik.components.input.mInput
import com.ccfraser.muirwik.components.input.mInputAdornment
import com.ccfraser.muirwik.components.input.mInputLabel
import com.ccfraser.muirwik.components.mButton
import com.ccfraser.muirwik.components.mIconButton
import com.ccfraser.muirwik.components.mTextField
import com.ccfraser.muirwik.components.mTypography
import com.ccfraser.muirwik.components.targetInputValue
import kotlinx.css.Align
import kotlinx.css.FlexDirection
import kotlinx.css.marginBottom
import kotlinx.css.px
import kotlinx.html.InputType
import materialui.icons.iconVisibility
import materialui.icons.iconVisibilityOff
import pl.karol202.sciorder.client.js.common.util.flexBox
import pl.karol202.sciorder.client.js.common.util.onEnterEventListener
import pl.karol202.sciorder.client.js.common.util.prop
import pl.karol202.sciorder.client.js.common.view.View
import pl.karol202.sciorder.client.js.common.viewmodel.OwnerJsViewModel
import react.RBuilder
import react.RProps
import react.RState
import react.setState
import styled.css
import styled.styledDiv

class AdminLoginView(props: Props) : View<AdminLoginView.Props, AdminLoginView.State>(props)
{
	interface Props : RProps
	{
		var ownerViewModel: OwnerJsViewModel
	}

	interface State : RState
	{
		var ownerName: String
		var password: String
		var showPassword: Boolean
	}

	private val ownerViewModel by prop { ownerViewModel }

	init
	{
		state.ownerName = ""
		state.password = ""
		state.showPassword = false
	}

	override fun RBuilder.render()
	{
		flexBox(direction = FlexDirection.column,
		        alignItems = Align.stretch) {
			titleText()
			ownerTextField()
			passwordTextField()
			loginButton()
			registerButton()
		}
	}

	private fun RBuilder.titleText() = mTypography("Panel administracyjny", variant = MTypographyVariant.h5)

	private fun RBuilder.ownerTextField() = mTextField("Konto",
	                                           fullWidth = true,
	                                           value = state.ownerName,
	                                           onChange = { setOwnerName(it.targetInputValue) })

	private fun RBuilder.passwordTextField() = styledDiv {
		css {
			marginBottom = 16.px
		}
		mFormControl(fullWidth = true) {
			mInputLabel(caption = "Hasło", htmlFor = "input-password")
			mInput(id = "input-password",
			       type = if(state.showPassword) InputType.text else InputType.password,
			       autoComplete = "current-password",
			       value = state.password,
			       onChange = { setPassword(it.targetInputValue) },
			       endAdornment =
			       mInputAdornment(position = MInputAdornmentPosition.end) {
				       mIconButton(onClick = { togglePasswordVisibility() }) {
					       if(state.showPassword) iconVisibilityOff()
					       else iconVisibility()
				       }
			       }) {
				onEnterEventListener { login() }
			}
		}
	}

	private fun RBuilder.loginButton() = styledDiv {
		css {
			marginBottom = 16.px
		}
		mButton("Zaloguj",
		        color = MColor.secondary,
		        variant = MButtonVariant.outlined,
		        fullWidth = true,
		        onClick = { login() })
	}

	private fun RBuilder.registerButton() = mButton("Utwórz nowe konto",
	                                        color = MColor.secondary,
	                                        variant = MButtonVariant.outlined,
	                                        fullWidth = true,
	                                        onClick = { register() })

	private fun setOwnerName(newName: String) = setState { ownerName = newName }

	private fun setPassword(newPassword: String) = setState { password = newPassword }

	private fun togglePasswordVisibility() = setState { showPassword = !showPassword }

	private fun login()
	{
		ownerViewModel.login(state.ownerName, state.password)
	}

	private fun register()
	{
		ownerViewModel.register(state.ownerName, state.password)
	}
}

fun RBuilder.adminLoginView(ownerViewModel: OwnerJsViewModel) = child(AdminLoginView::class) {
	attrs.ownerViewModel = ownerViewModel
}
