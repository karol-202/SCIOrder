package pl.karol202.sciorder.client.js.common.util

import kotlinx.css.Align
import kotlinx.css.Display
import kotlinx.css.FlexBasis
import kotlinx.css.FlexDirection
import kotlinx.css.FlexWrap
import kotlinx.css.JustifyContent
import kotlinx.css.alignItems
import kotlinx.css.alignSelf
import kotlinx.css.display
import kotlinx.css.flexBasis
import kotlinx.css.flexDirection
import kotlinx.css.flexGrow
import kotlinx.css.flexShrink
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
import styled.StyledBuilder
import styled.StyledDOMBuilder
import styled.css
import styled.styledDiv
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

fun <T, P : RProps> RComponent<P, *>.prop(selector: P.() -> T) = object : ReadOnlyProperty<Any, T>
{
	override fun getValue(thisRef: Any, property: KProperty<*>) =
			props.selector() ?: throw IllegalArgumentException("Prop not passed: ${property.name}")
}

fun <T, P : RProps> RComponent<P, *>.nullableProp(selector: P.() -> T?) = object : ReadOnlyProperty<Any, T?>
{
	override fun getValue(thisRef: Any, property: KProperty<*>) = props.selector()
}

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

fun RBuilder.flexBox(direction: FlexDirection? = null,
                     wrap: FlexWrap? = null,
                     justifyContent: JustifyContent? = null,
                     alignItems: Align? = null,
                     styledHandler: StyledDOMBuilder<DIV>.() -> Unit) = styledDiv {
	flexBoxCss(direction, wrap, justifyContent, alignItems)
	styledHandler()
}

fun RBuilder.flexItem(grow: Double? = null,
                      shrink: Double? = null,
                      basis: FlexBasis? = null,
                      alignSelf: Align? = null,
                      styledHandler: StyledDOMBuilder<DIV>.() -> Unit) = styledDiv {
	flexItemCss(grow, shrink, basis, alignSelf)
	styledHandler()
}

fun RBuilder.flexBoxNested(direction: FlexDirection? = null,
                           wrap: FlexWrap? = null,
                           justifyContent: JustifyContent? = null,
                           alignItems: Align? = null,
                           grow: Double? = null,
                           shrink: Double? = null,
                           basis: FlexBasis? = null,
                           alignSelf: Align? = null,
                           styledHandler: StyledDOMBuilder<DIV>.() -> Unit) = styledDiv {
	flexBoxCss(direction, wrap, justifyContent, alignItems)
	flexItemCss(grow, shrink, basis, alignSelf)
	styledHandler()
}

private fun StyledBuilder<*>.flexBoxCss(direction: FlexDirection?,
                                        wrap: FlexWrap?,
                                        justifyContent: JustifyContent?,
                                        alignItems: Align?) = css {
	display = Display.flex
	direction?.let { this.flexDirection = it }
	wrap?.let { this.flexWrap = it }
	justifyContent?.let { this.justifyContent = it }
	alignItems?.let { this.alignItems = it }
}

private fun StyledBuilder<*>.flexItemCss(grow: Double?,
                                         shrink: Double?,
                                         basis: FlexBasis?,
                                         alignSelf: Align?) = css {
	grow?.let { this.flexGrow = it }
	shrink?.let { this.flexShrink = it }
	basis?.let { this.flexBasis = it }
	alignSelf?.let { this.alignSelf = it }
}
