package pl.karol202.sciorder.client.js.common.view

import com.ccfraser.muirwik.components.mPaper
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

class MainView(props: Props) : View<MainView.Props, RState>(props)
{
	interface Props : RProps
	{
		var viewModels: ViewModels
	}
	
	private val viewModels by prop { viewModels }

	override fun RBuilder.render()
	{
		switch {
			route<RProps>("/admin") { (_, _, match) ->
				loginControlView(viewModels.adminOwnerViewModel, match, { adminLoginPanel() }, { adminView() })
			}
			route<RProps>("/user") { (_, _, match) ->
				loginControlView(viewModels.userOwnerViewModel, match, { userLoginPanel() }, { userView() })
			}
			routeElse { redirectTo("/user") }
		}
	}

	private fun adminLoginPanel() = loginSheet { adminLoginView(viewModels.adminOwnerViewModel) }

	private fun userLoginPanel() = loginSheet { userLoginView(viewModels.userOwnerViewModel) }

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
		adminView(viewModels.productsEditViewModel, viewModels.ordersViewModel)
	}
	
	private fun userView() = buildElement {
		userView(viewModels.productsViewModel, viewModels.orderComposeViewModel, viewModels.ordersTrackViewModel)
	}
}

fun RBuilder.mainView(viewModels: ViewModels) = child(MainView::class) {
	attrs.viewModels = viewModels
}
