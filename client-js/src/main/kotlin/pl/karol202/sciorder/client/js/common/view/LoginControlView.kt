package pl.karol202.sciorder.client.js.common.view

import pl.karol202.sciorder.client.js.common.util.*
import pl.karol202.sciorder.client.js.common.viewmodel.ViewModels
import react.RBuilder
import react.RProps
import react.RState
import react.ReactElement
import react.router.dom.RouteResultMatch
import react.router.dom.route
import react.router.dom.switch

class LoginControlView : ExtendedComponent<LoginControlView.Props, LoginControlView.State>()
{
	interface Props : RProps
	{
		var viewModels: ViewModels?
		var match: RouteResultMatch<RProps>?
		var loginView: ((RouteResultMatch<RProps>) -> ReactElement?)?
		var restView: ((RouteResultMatch<RProps>) -> ReactElement?)?
	}

	interface State : RState
	{
		var loggedIn: Boolean
	}

	private val viewModels by prop { viewModels }
	private val match by prop { match }
	private val loginView by prop { loginView }
	private val restView by prop { restView }

	override fun State.init(props: Props)
	{
		loggedIn = false
		viewModels.ownerViewModel.ownerObservable.bindToState { loggedIn = it != null }
	}

	override fun RBuilder.render()
	{
		switch {
			route<RProps>("${match.path}/login") { (_, _, match) ->
				loginView(match)
			}
			routeElse { (_, _, match) ->
				if(state.loggedIn) restView(match)
				else redirectTo("${match.url}/login")
			}
		}
	}
}

fun RBuilder.loginControlView(viewModels: ViewModels,
                              match: RouteResultMatch<RProps>,
                              loginView: (RouteResultMatch<RProps>) -> ReactElement?,
                              restView: (RouteResultMatch<RProps>) -> ReactElement?) = child(LoginControlView::class) {
	attrs.viewModels = viewModels
	attrs.match = match
	attrs.loginView = loginView
	attrs.restView = restView
}
