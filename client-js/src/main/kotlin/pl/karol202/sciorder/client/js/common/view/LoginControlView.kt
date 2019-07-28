package pl.karol202.sciorder.client.js.common.view

import com.ccfraser.muirwik.components.*
import kotlinx.css.Color
import kotlinx.css.FlexDirection
import pl.karol202.sciorder.client.common.viewmodel.OwnerViewModel
import pl.karol202.sciorder.client.js.common.util.*
import pl.karol202.sciorder.client.js.common.viewmodel.OwnerJsViewModel
import react.*
import react.router.dom.RouteResultMatch
import react.router.dom.route
import react.router.dom.switch
import styled.styledDiv

class LoginControlView(props: Props) : View<LoginControlView.Props, LoginControlView.State>(props)
{
	interface Props : RProps
	{
		var ownerViewModel: OwnerJsViewModel
		var match: RouteResultMatch<RProps>
		var loginView: (RouteResultMatch<RProps>) -> ReactElement?
		var restView: (RouteResultMatch<RProps>) -> ReactElement?
	}

	interface State : RState
	{
		var loggedIn: Boolean
		var ownerName: String?
		
		var lastError: OwnerViewModel.Error?
		var errorShown: Boolean
	}

	private val ownerViewModel by prop { ownerViewModel }
	private val mainMatch by prop { match }
	private val loginView by prop { loginView }
	private val restView by prop { restView }

	init
	{
		state.loggedIn = false
		state.errorShown = false

		ownerViewModel.ownerObservable.bindToState {
			loggedIn = it != null
			ownerName = it?.name
		}
		ownerViewModel.errorEventObservable.bindEventToState {
			lastError = it
			errorShown = true
		}
	}

	override fun RBuilder.render()
	{
		styledDiv {
			cssFlexBox(direction = FlexDirection.column)
			
			appBar()
			contentView()
		}
		snackbar()
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

	private fun RBuilder.contentView() = styledDiv {
		cssFlexItem(grow = 1.0)
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
	
	private fun logout() = ownerViewModel.logout()
	
	private fun hideSnackbar() = setState { errorShown = false }
}

fun RBuilder.loginControlView(ownerViewModel: OwnerJsViewModel,
                              match: RouteResultMatch<RProps>,
                              loginView: (RouteResultMatch<RProps>) -> ReactElement?,
                              restView: (RouteResultMatch<RProps>) -> ReactElement?) = child(LoginControlView::class) {
	attrs.ownerViewModel = ownerViewModel
	attrs.match = match
	attrs.loginView = loginView
	attrs.restView = restView
}
