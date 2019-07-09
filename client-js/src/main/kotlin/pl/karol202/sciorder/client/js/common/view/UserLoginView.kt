package pl.karol202.sciorder.client.js.common.view

import com.ccfraser.muirwik.components.*
import kotlinx.css.FlexDirection
import kotlinx.css.flexDirection
import kotlinx.css.marginBottom
import kotlinx.css.px
import pl.karol202.sciorder.client.js.common.util.ExtendedComponent
import pl.karol202.sciorder.client.js.common.util.prop
import pl.karol202.sciorder.client.js.common.viewmodel.OwnerJsViewModel
import react.RBuilder
import react.RProps
import react.RState
import react.setState
import styled.css
import styled.styledDiv

class UserLoginView : ExtendedComponent<UserLoginView.Props, UserLoginView.State>()
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

	override fun State.init()
	{
		ownerName = ""
	}

	override fun RBuilder.render()
	{
		mGridContainer(alignItems = MGridAlignItems.stretch) {
			css {
				flexDirection = FlexDirection.column
			}

			mGridItem {
				mTypography("Zaloguj się", variant = MTypographyVariant.h5)
			}
			mGridItem {
				styledDiv {
					css {
						marginBottom = 16.px
					}
					mTextField("Konto",
					           fullWidth = true,
					           value = state.ownerName,
					           onChange = { onOwnerNameChange(it.targetInputValue) })
				}
			}
			mGridItem {
				mButton("Zaloguj",
				        color = MColor.secondary,
				        variant = MButtonVariant.contained,
				        fullWidth = true,
				        onClick = { login() })
			}
		}
	}

	private fun onOwnerNameChange(newName: String) = setState { ownerName = newName }

	private fun login()
	{
		ownerViewModel.login(state.ownerName)
	}
}

fun RBuilder.userLoginView(ownerViewModel: OwnerJsViewModel) = child(UserLoginView::class) {
	attrs.ownerViewModel = ownerViewModel
}
