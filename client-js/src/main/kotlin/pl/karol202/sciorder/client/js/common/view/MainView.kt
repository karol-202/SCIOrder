package pl.karol202.sciorder.client.js.common.view

import com.ccfraser.muirwik.components.*
import kotlinx.css.*
import pl.karol202.sciorder.client.js.common.util.*
import pl.karol202.sciorder.client.js.common.view.admin.adminLoginView
import pl.karol202.sciorder.client.js.common.view.admin.adminView
import pl.karol202.sciorder.client.js.common.view.user.userLoginView
import pl.karol202.sciorder.client.js.common.view.user.userView
import pl.karol202.sciorder.client.js.common.viewmodel.ViewModels
import react.RBuilder
import react.RProps
import react.RState
import react.buildElement
import react.router.dom.route
import react.router.dom.switch
import styled.css
import styled.styledDiv

class MainView(props: Props) : View<MainView.Props, MainView.State>(props)
{
	interface Props : RProps
	{
		var viewModels: ViewModels
	}

	interface State : RState
	{
		var loggedIn: Boolean
		var ownerName: String?
	}

	private val viewModels by prop { viewModels }

	init
	{
		state.loggedIn = false

		viewModels.ownerViewModel.ownerObservable.bindToState {
			loggedIn = it != null
			ownerName = it?.name
		}
	}

	override fun RBuilder.render()
	{
		styledDiv {
			cssFlexBox(direction = FlexDirection.column)
			
			appBar()
			
			styledDiv {
				cssFlexItem(grow = 1.0)
				switch {
					route<RProps>("/admin") { (_, _, match) ->
						loginControlView(viewModels, match, { adminLoginPanel() }, { adminView() })
					}
					route<RProps>("/user") { (_, _, match) ->
						loginControlView(viewModels, match, { userLoginPanel() }, { userView() })
					}
					routeElse { redirectTo("/user") }
				}
			}
		}
	}

	private fun RBuilder.appBar() = mAppBar(position = MAppBarPosition.sticky) {
		mToolbar {
			mTypography(state.ownerName ?: "SCIOrder",
			            variant = MTypographyVariant.h6,
			            color = MTypographyColor.inherit,
			            noWrap = true) {
				cssFlexItem(grow = 1.0)
			}
			if(state.loggedIn) mIconButton(color = MColor.inherit,
			                               onClick = { logout() }) { iconLogout() }
		}
	}

	private fun adminLoginPanel() = loginSheet { adminLoginView(viewModels.ownerViewModel) }

	private fun userLoginPanel() = loginSheet { userLoginView(viewModels.ownerViewModel) }

	private fun loginSheet(handler: RBuilder.() -> Unit) = buildElement {
		mPaper {
			css {
				width = 400.px
				margin(horizontal = LinearDimension.auto, vertical = 64.px)
				padding(24.px)
			}
			handler()
		}
	}
	
	private fun adminView() = buildElement {
		adminView(viewModels.productsViewModel, viewModels.ordersViewModel)
	}
	
	private fun userView() = buildElement {
		userView(viewModels.productsViewModel, viewModels.orderComposeViewModel, viewModels.ordersTrackViewModel)
	}

	private fun logout() = viewModels.ownerViewModel.logout()
}

fun RBuilder.mainView(viewModels: ViewModels) = child(MainView::class) {
	attrs.viewModels = viewModels
}
