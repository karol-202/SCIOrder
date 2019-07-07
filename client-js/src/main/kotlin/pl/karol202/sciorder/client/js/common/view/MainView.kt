package pl.karol202.sciorder.client.js.common.view

import com.ccfraser.muirwik.components.*
import kotlinx.css.*
import pl.karol202.sciorder.client.js.common.util.*
import pl.karol202.sciorder.client.js.common.viewmodel.ViewModels
import react.RBuilder
import react.RProps
import react.RState
import react.dom.div
import react.router.dom.route
import react.router.dom.switch
import styled.css

class MainView : ExtendedComponent<MainView.Props, RState>()
{
	interface Props : RProps
	{
		var viewModels: ViewModels?
	}

	private val viewModels by prop { viewModels }

	override fun RBuilder.render()
	{
		mAppBar(position = MAppBarPosition.relative) {
			mToolbar {
				mToolbarTitle("SCIOrder")
			}
		}

		div {
			switch {
				route<RProps>("/admin") { (_, _, match) ->
					loginControlView(viewModels, match, { adminLoginSheet() }, { null })
				}
				route<RProps>("/user") { (_, _, match) ->
					loginControlView(viewModels, match, { userLoginSheet() }, { null })
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
}

fun RBuilder.mainView(viewModels: ViewModels) = child(MainView::class) {
	attrs.viewModels = viewModels
}
