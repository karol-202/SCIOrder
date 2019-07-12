package pl.karol202.sciorder.client.js.common.util

import kotlinx.css.Align
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.FlexWrap
import kotlinx.css.JustifyContent
import kotlinx.css.alignItems
import kotlinx.css.display
import kotlinx.css.flexDirection
import kotlinx.css.flexWrap
import kotlinx.css.justifyContent
import kotlinx.html.DIV
import react.RBuilder
import react.RComponent
import react.RElementBuilder
import react.RProps
import react.ReactElement
import react.router.dom.RouteResultProps
import react.router.dom.redirect
import react.router.dom.route
import styled.StyledDOMBuilder
import styled.css
import styled.styledDiv
import kotlin.reflect.KClass

fun RBuilder.routeElse(render: (RouteResultProps<RProps>) -> ReactElement?) = route("", render = render)

fun RBuilder.routeElse(component: KClass<out RComponent<*, *>>) = route("", component)

fun RBuilder.redirectTo(to: String) = redirect(from = "", to = to)

operator fun <T : RProps> RouteResultProps<T>.component1() = history

operator fun <T : RProps> RouteResultProps<T>.component2() = location

operator fun <T : RProps> RouteResultProps<T>.component3() = match

fun RElementBuilder<*>.onEnterEventListener(onEnter: () -> Unit)
{
	attrs.asDynamic().onKeyDown = { event: dynamic -> if(event.key == "Enter") onEnter() }
}

fun RBuilder.flexBox(flexDirection: FlexDirection? = null,
                     flexWrap: FlexWrap? = null,
                     justifyContent: JustifyContent? = null,
                     alignItems: Align? = null,
                     styledHandler: StyledDOMBuilder<DIV>.() -> Unit) = styledDiv {
	css {
		display = Display.flex
		flexDirection?.let { this.flexDirection = it }
		flexWrap?.let { this.flexWrap = it }
		justifyContent?.let { this.justifyContent = it }
		alignItems?.let { this.alignItems = it }
	}
	styledHandler()
}
