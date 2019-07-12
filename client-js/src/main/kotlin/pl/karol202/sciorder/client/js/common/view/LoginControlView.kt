package pl.karol202.sciorder.client.js.common.view

import com.ccfraser.muirwik.components.currentTheme
import com.ccfraser.muirwik.components.mSnackbar
import kotlinx.css.Color
import kotlinx.css.backgroundColor
import pl.karol202.sciorder.client.common.viewmodel.OwnerViewModel
import pl.karol202.sciorder.client.js.common.util.component1
import pl.karol202.sciorder.client.js.common.util.component2
import pl.karol202.sciorder.client.js.common.util.component3
import pl.karol202.sciorder.client.js.common.util.prop
import pl.karol202.sciorder.client.js.common.util.redirectTo
import pl.karol202.sciorder.client.js.common.util.routeElse
import pl.karol202.sciorder.client.js.common.viewmodel.ViewModels
import react.RBuilder
import react.RProps
import react.RState
import react.ReactElement
import react.router.dom.RouteResultMatch
import react.router.dom.route
import react.router.dom.switch
import react.setState
import styled.css

class LoginControlView(props: Props) : View<LoginControlView.Props, LoginControlView.State>(props)
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
		var lastError: OwnerViewModel.Error?
		var showError: Boolean
	}

	private val viewModels by prop { viewModels }
	private val mainMatch by prop { match }
	private val loginView by prop { loginView }
	private val restView by prop { restView }

	init
	{
		state.loggedIn = false
		state.showError = false

		viewModels.ownerViewModel.ownerObservable.bindToState { loggedIn = it != null }
		viewModels.ownerViewModel.errorEventObservable.bindEventToState { lastError = it; showError = true }
	}

	override fun RBuilder.render()
	{
		switch {
			route<RProps>("${mainMatch.path}/login") { (_, _, match) ->
				if(!state.loggedIn) loginView(match)
				else redirectTo(mainMatch.url)
			}
			routeElse { (_, _, match) ->
				if(state.loggedIn) restView(match)
				else redirectTo("${match.url}/login")
			}
		}
		snackbar()
	}

	private fun RBuilder.snackbar(): ReactElement
	{
		val message = when(state.lastError)
		{
			OwnerViewModel.Error.NOT_FOUND -> "Dane nieprawidłowe"
			OwnerViewModel.Error.NAME_BUSY -> "Nazwa zajęta"
			OwnerViewModel.Error.OTHER -> "Błąd logowania"
			null -> ""
		}
		return mSnackbar(message = message, open = state.showError, autoHideDuration = 3000, onClose = { _, _ -> hideSnackbar() }) {
			css {
				child(".MuiSnackbarContent-root") {
					backgroundColor = Color(currentTheme.palette.error.main)
				}
			}
		}
	}

	private fun hideSnackbar() = setState { showError = false }
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
