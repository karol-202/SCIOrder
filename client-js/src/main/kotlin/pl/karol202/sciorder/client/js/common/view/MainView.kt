package pl.karol202.sciorder.client.js.common.view

import com.ccfraser.muirwik.components.*
import kotlinx.css.*
import kotlinx.html.classes
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
import react.router.dom.LinkComponent
import react.router.dom.route
import react.router.dom.switch
import styled.css
import styled.styledDiv

class MainView(props: Props) : View<MainView.Props, RState>(props)
{
	interface Props : RProps
	{
		var viewModels: ViewModels
	}
	
	companion object
	{
		private const val GITHUB_LINK = "https://github.com/karol-202/SCIOrder"
	}
	
	private val viewModels by prop { viewModels }

	override fun RBuilder.render()
	{
		styledDiv {
			cssFlexBox(direction = FlexDirection.column)
			css { minHeight = 100.vh }
			mainSwitch()
			footer()
		}
	}

	private fun RBuilder.mainSwitch() = styledDiv {
		cssFlexItem(grow = 1.0, shrink = 0.0)
		
		switch {
			route<RProps>("/admin") { (_, _, match) ->
				loginControlView(viewModels.adminOwnerViewModel, match, { adminLoginPanel() }, { adminView() })
			}
			route<RProps>("/user") { (_, _, match) ->
				loginControlView(viewModels.userOwnerViewModel, match, { userLoginPanel() }, { userView() })
			}
			redirectTo("/user")
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
	
	private fun RBuilder.footer() = styledDiv {
		attrs.classes += "MuiPaper-elevation4"
		divider()
		styledDiv {
			cssFlexBox(direction = FlexDirection.row)
			styledDiv {
				cssFlexItem(basis = 600.px.basis)
				cssFlexBox(direction = FlexDirection.row,
				           alignItems = Align.center,
				           justifyContent = JustifyContent.spaceBetween)
				css {
					margin(horizontal = LinearDimension.auto)
					padding(top = 11.px, bottom = 4.px)
				}
				
				authorText()
				modePanel()
				githubLink()
			}
		}
	}
	
	private fun RBuilder.authorText() = mTypography(text = "SCIOrder by Karol Jurski",
	                                                variant = MTypographyVariant.body2)
	
	private fun RBuilder.modePanel() = styledDiv {
		cssFlexBox(direction = FlexDirection.row)
		
		mLink(text = "Użytkownik",
		      variant = MTypographyVariant.subtitle2,
		      color = MTypographyColor.secondary) {
			attrs.asDynamic().component = LinkComponent::class.js
			attrs.asDynamic().to = "/user"
		}
		mTypography(text = "|",
		            variant = MTypographyVariant.subtitle2) {
			overrideCss { margin(horizontal = 6.px) }
		}
		mLink(text = "Panel administracyjny",
		      variant = MTypographyVariant.subtitle2,
		      color = MTypographyColor.secondary) {
			attrs.asDynamic().component = LinkComponent::class.js
			attrs.asDynamic().to = "/admin"
		}
	}
	
	private fun RBuilder.githubLink() = mLink(href = GITHUB_LINK,
	                                          color = MTypographyColor.textPrimary) {
		iconGithub {
			overrideCss {
				width = 1.3.em
				height = 1.3.em
				margin(horizontal = LinearDimension.auto)
			}
		}
		mTypography(text = "Zobacz na Githubie",
		            variant = MTypographyVariant.subtitle2)
	}
}

fun RBuilder.mainView(viewModels: ViewModels) = child(MainView::class) {
	attrs.viewModels = viewModels
}
