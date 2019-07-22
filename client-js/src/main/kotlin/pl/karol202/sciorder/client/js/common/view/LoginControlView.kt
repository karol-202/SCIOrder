package pl.karol202.sciorder.client.js.common.view

import com.ccfraser.muirwik.components.currentTheme
import com.ccfraser.muirwik.components.mSnackbar
import kotlinx.css.Color
import pl.karol202.sciorder.client.common.viewmodel.OwnerViewModel
import pl.karol202.sciorder.client.js.common.util.*
import pl.karol202.sciorder.client.js.common.viewmodel.ViewModels
import react.*
import react.router.dom.RouteResultMatch
import react.router.dom.route
import react.router.dom.switch

class LoginControlView(props: Props) : View<LoginControlView.Props, LoginControlView.State>(props)
{
	interface Props : RProps
	{
		var viewModels: ViewModels
		var match: RouteResultMatch<RProps>
		var loginView: (RouteResultMatch<RProps>) -> ReactElement?
		var restView: (RouteResultMatch<RProps>) -> ReactElement?
	}

	interface State : RState
	{
		var loggedIn: Boolean
		var lastError: OwnerViewModel.Error?
		var errorShown: Boolean
	}

	private val viewModels by prop { viewModels }
	private val mainMatch by prop { match }
	private val loginView by prop { loginView }
	private val restView by prop { restView }

	init
	{
		state.loggedIn = false
		state.errorShown = false

		viewModels.ownerViewModel.ownerObservable.bindToState { loggedIn = it != null }
		viewModels.ownerViewModel.errorEventObservable.bindEventToState { lastError = it; errorShown = true }
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
			null -> ""
			OwnerViewModel.Error.NOT_FOUND -> "Dane nieprawidłowe"
			OwnerViewModel.Error.NAME_BUSY -> "Nazwa zajęta"
			OwnerViewModel.Error.NAME_INVALID -> "Nazwa za krótka"
			OwnerViewModel.Error.PASSWORD_TOO_SHORT -> "Hasło za krótkie"
			else -> "Błąd logowania"
		}
		return mSnackbar(message = message,
		                 autoHideDuration = 3000,
		                 open = state.errorShown,
		                 onClose = { _, _ -> hideSnackbar() }) {
			cssSnackbarColor(Color(currentTheme.palette.error.main))
		}
	}

	private fun hideSnackbar() = setState { errorShown = false }
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
