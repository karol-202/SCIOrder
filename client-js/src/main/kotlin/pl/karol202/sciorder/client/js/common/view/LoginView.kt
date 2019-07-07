package pl.karol202.sciorder.client.js.common.view

import com.ccfraser.muirwik.components.*
import kotlinx.css.*
import pl.karol202.sciorder.client.js.common.util.ExtendedComponent
import pl.karol202.sciorder.client.js.common.util.mTextFieldColor
import pl.karol202.sciorder.client.js.common.util.prop
import pl.karol202.sciorder.client.js.common.viewmodel.OwnerJsViewModel
import react.RBuilder
import react.RProps
import react.RState
import styled.css

class LoginView : ExtendedComponent<LoginView.Props, RState>()
{
	interface Props : RProps
	{
		var ownerViewModel: OwnerJsViewModel?
	}

	private val ownerViewModel by prop { ownerViewModel }

	override fun RBuilder.render()
	{
		mGridContainer(alignItems = MGridAlignItems.stretch) {
			css {
				flexDirection = FlexDirection.column
			}

			mGridItem {
				mTypography("Podaj nazwę konta", variant = MTypographyVariant.h5)
			}
			mGridItem {
				mTextFieldColor(textFieldColor = MColor.secondary) {
					mTextField("Konto", fullWidth = true) {
						css {
							marginBottom = 24.px
						}
					}
				}
			}
			mGridItem {
				mButton("Zaloguj", color = MColor.secondary, variant = MButtonVariant.contained, fullWidth = true)
			}
		}
	}
}

fun RBuilder.loginView(ownerViewModel: OwnerJsViewModel) = child(LoginView::class) {
	attrs.ownerViewModel = ownerViewModel
}

fun RBuilder.loginSheet(ownerViewModel: OwnerJsViewModel) =
		mPaper {
			css {
				width = 400.px
				margin(horizontal = LinearDimension.auto, vertical = 64.px)
				padding(24.px)
			}
			loginView(ownerViewModel)
		}
