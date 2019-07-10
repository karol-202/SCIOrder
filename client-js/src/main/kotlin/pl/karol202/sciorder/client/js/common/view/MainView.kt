package pl.karol202.sciorder.client.js.common.view

import com.ccfraser.muirwik.components.*
import kotlinx.css.*
import materialui.icons.iconLogout
import pl.karol202.sciorder.client.js.common.util.*
import pl.karol202.sciorder.client.js.common.viewmodel.ViewModels
import react.RBuilder
import react.RProps
import react.RState
import react.dom.div
import react.router.dom.route
import react.router.dom.switch
import styled.css

class MainView : ExtendedComponent<MainView.Props, MainView.State>()
{
	interface Props : RProps
	{
		var viewModels: ViewModels?
	}

	interface State : RState
	{
		var loggedIn: Boolean
		var ownerName: String?
	}

	private val viewModels by prop { viewModels }

	override fun State.init()
	{
		loggedIn = false
	}

	override fun componentDidMount() = viewModels.ownerViewModel.run {
		ownerObservable.bindToState { loggedIn = it != null }
		ownerObservable.bindToState { ownerName = it?.name }
	}

	override fun RBuilder.render()
	{
		mAppBar(position = MAppBarPosition.relative) {
			mToolbar {
				mTypography(state.ownerName ?: "SCIOrder",
				            variant = MTypographyVariant.h6,
				            color = MTypographyColor.inherit,
				            noWrap = true) {
					css {
						flexGrow = 1.0
					}
				}
				if(state.loggedIn) mIconButton(color = MColor.inherit,
				                               onClick = { logout() }) { iconLogout() }
			}
		}

		div {
			switch {
				route<RProps>("/admin") { (_, _, match) ->
					loginControlView(viewModels,
					                 match,
					                 { adminLoginSheet() },
					                 { null })
				}
				route<RProps>("/user") { (_, _, match) ->
					loginControlView(viewModels,
					                 match,
					                 { userLoginSheet() },
					                 { userView(viewModels.productsViewModel, viewModels.ordersTrackViewModel) })
				}
				routeElse {
					redirectTo("/user")
				}
			}
		}
	}

	private fun RBuilder.adminLoginSheet() = loginSheet { adminLoginView(viewModels.ownerViewModel) }

	private fun RBuilder.userLoginSheet() = loginSheet { userLoginView(viewModels.ownerViewModel) }

	private fun RBuilder.loginSheet(handler: RBuilder.() -> Unit) =
			mPaper {
				css {
					width = 400.px
					margin(horizontal = LinearDimension.auto, vertical = 64.px)
					padding(24.px)
				}
				handler()
			}

	private fun logout() = viewModels.ownerViewModel.logout()
}

fun RBuilder.mainView(viewModels: ViewModels) = child(MainView::class) {
	attrs.viewModels = viewModels
}
