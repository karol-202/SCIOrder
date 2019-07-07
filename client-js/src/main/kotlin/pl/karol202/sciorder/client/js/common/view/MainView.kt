package pl.karol202.sciorder.client.js.common.view

import com.ccfraser.muirwik.components.MAppBarPosition
import com.ccfraser.muirwik.components.mAppBar
import com.ccfraser.muirwik.components.mToolbar
import com.ccfraser.muirwik.components.mToolbarTitle
import pl.karol202.sciorder.client.js.common.util.*
import pl.karol202.sciorder.client.js.common.viewmodel.ViewModels
import react.RBuilder
import react.RProps
import react.RState
import react.dom.div
import react.router.dom.route
import react.router.dom.switch

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
					loginControlView(viewModels, match, { loginSheet(viewModels.ownerViewModel) }, { null })
				}
				route<RProps>("/user") { (_, _, match) ->
					loginControlView(viewModels, match, { loginSheet(viewModels.ownerViewModel) }, { null })
				}
				routeElse {
					redirectTo("/user")
				}
			}
		}
	}
}

fun RBuilder.mainView(viewModels: ViewModels) = child(MainView::class) {
	attrs.viewModels = viewModels
}
