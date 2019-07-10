package pl.karol202.sciorder.client.js.common.view

import com.ccfraser.muirwik.components.*
import com.ccfraser.muirwik.components.form.mFormControl
import com.ccfraser.muirwik.components.input.MInputAdornmentPosition
import com.ccfraser.muirwik.components.input.mInput
import com.ccfraser.muirwik.components.input.mInputAdornment
import com.ccfraser.muirwik.components.input.mInputLabel
import kotlinx.css.FlexDirection
import kotlinx.css.flexDirection
import kotlinx.css.marginBottom
import kotlinx.css.px
import kotlinx.html.InputType
import materialui.icons.iconVisibility
import materialui.icons.iconVisibilityOff
import pl.karol202.sciorder.client.js.common.util.ExtendedComponent
import pl.karol202.sciorder.client.js.common.util.onEnterEventListener
import pl.karol202.sciorder.client.js.common.util.prop
import pl.karol202.sciorder.client.js.common.viewmodel.OwnerJsViewModel
import react.RBuilder
import react.RProps
import react.RState
import react.setState
import styled.css
import styled.styledDiv

class AdminLoginView : ExtendedComponent<AdminLoginView.Props, AdminLoginView.State>()
{
	interface Props : RProps
	{
		var ownerViewModel: OwnerJsViewModel?
	}

	interface State : RState
	{
		var ownerName: String
		var password: String
		var showPassword: Boolean
	}

	private val ownerViewModel by prop { ownerViewModel }

	override fun State.init()
	{
		ownerName = ""
		password = ""
		showPassword = false
	}

	override fun RBuilder.render()
	{
		mGridContainer(alignItems = MGridAlignItems.stretch) {
			css {
				flexDirection = FlexDirection.column
			}

			mGridItem {
				mTypography("Panel administracyjny", variant = MTypographyVariant.h5)
			}
			mGridItem {
				mTextField("Konto",
				           fullWidth = true,
				           value = state.ownerName,
				           onChange = { onOwnerNameChange(it.targetInputValue) })
			}
			mGridItem {
				styledDiv {
					css {
						marginBottom = 16.px
					}
					mFormControl(fullWidth = true) {
						mInputLabel(caption = "Hasło", htmlFor = "input-password")
						mInput(id = "input-password",
						       type = if(state.showPassword) InputType.text else InputType.password,
						       autoComplete = "current-password",
						       value = state.password,
						       onChange = { onPasswordChange(it.targetInputValue) },
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
			}
			mGridItem {
				styledDiv {
					css {
						marginBottom = 16.px
					}
					mButton("Zaloguj",
					        color = MColor.secondary,
					        variant = MButtonVariant.contained,
					        fullWidth = true,
					        onClick = { login() })
				}
			}
			mGridItem {
				mButton("Utwórz nowe konto",
				        color = MColor.secondary,
				        variant = MButtonVariant.contained,
				        fullWidth = true,
				        onClick = { register() })
			}
		}
	}

	private fun onOwnerNameChange(newName: String) = setState { ownerName = newName }

	private fun onPasswordChange(newPassword: String) = setState { password = newPassword }

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
