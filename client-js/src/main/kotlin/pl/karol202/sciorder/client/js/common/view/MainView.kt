package pl.karol202.sciorder.client.js.common.view

import com.ccfraser.muirwik.components.MAppBarPosition
import com.ccfraser.muirwik.components.MColor
import com.ccfraser.muirwik.components.MTypographyColor
import com.ccfraser.muirwik.components.MTypographyVariant
import com.ccfraser.muirwik.components.mAppBar
import com.ccfraser.muirwik.components.mIconButton
import com.ccfraser.muirwik.components.mPaper
import com.ccfraser.muirwik.components.mToolbar
import com.ccfraser.muirwik.components.mTypography
import kotlinx.css.FlexDirection
import kotlinx.css.LinearDimension
import kotlinx.css.flexGrow
import kotlinx.css.height
import kotlinx.css.margin
import kotlinx.css.padding
import kotlinx.css.pct
import kotlinx.css.px
import kotlinx.css.width
import materialui.icons.iconLogout
import pl.karol202.sciorder.client.js.common.util.component1
import pl.karol202.sciorder.client.js.common.util.component2
import pl.karol202.sciorder.client.js.common.util.component3
import pl.karol202.sciorder.client.js.common.util.flexBox
import pl.karol202.sciorder.client.js.common.util.prop
import pl.karol202.sciorder.client.js.common.util.redirectTo
import pl.karol202.sciorder.client.js.common.util.routeElse
import pl.karol202.sciorder.client.js.common.view.admin.adminLoginView
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

		viewModels.ownerViewModel.ownerObservable.bindToState { loggedIn = it != null }
		viewModels.ownerViewModel.ownerObservable.bindToState { ownerName = it?.name }
	}

	override fun RBuilder.render()
	{
		flexBox(flexDirection = FlexDirection.column) {
			css {
				height = 100.pct
			}
			
			appBar()
			
			styledDiv {
				css {
					flexGrow = 1.0
				}
				
				switch {
					route<RProps>("/admin") { (_, _, match) ->
						loginControlView(viewModels, match, { adminLoginSheet() }, { null })
					}
					route<RProps>("/user") { (_, _, match) ->
						loginControlView(viewModels, match, { userLoginSheet() }, { userView(viewModels.productsViewModel, viewModels.orderComposeViewModel, viewModels.ordersTrackViewModel) })
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
				css {
					flexGrow = 1.0
				}
			}
			if(state.loggedIn) mIconButton(color = MColor.inherit,
			                               onClick = { logout() }) { iconLogout() }
		}
	}

	private fun adminLoginSheet() = loginSheet { adminLoginView(viewModels.ownerViewModel) }

	private fun userLoginSheet() = loginSheet { userLoginView(viewModels.ownerViewModel) }

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

	private fun logout() = viewModels.ownerViewModel.logout()
}

fun RBuilder.mainView(viewModels: ViewModels) = child(MainView::class) {
	attrs.viewModels = viewModels
}
