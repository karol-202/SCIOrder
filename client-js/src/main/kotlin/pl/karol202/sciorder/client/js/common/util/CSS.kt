package pl.karol202.sciorder.client.js.common.util

import kotlinx.css.Align
import kotlinx.css.Color
import kotlinx.css.Display
import kotlinx.css.FlexBasis
import kotlinx.css.FlexDirection
import kotlinx.css.FlexWrap
import kotlinx.css.JustifyContent
import kotlinx.css.LinearDimension
import kotlinx.css.Position
import kotlinx.css.RuleSet
import kotlinx.css.alignItems
import kotlinx.css.alignSelf
import kotlinx.css.backgroundColor
import kotlinx.css.bottom
import kotlinx.css.display
import kotlinx.css.flexBasis
import kotlinx.css.flexDirection
import kotlinx.css.flexGrow
import kotlinx.css.flexShrink
import kotlinx.css.flexWrap
import kotlinx.css.justifyContent
import kotlinx.css.left
import kotlinx.css.position
import kotlinx.css.right
import kotlinx.css.top
import styled.StyledBuilder
import styled.css

fun StyledBuilder<*>.overrideCss(handler: RuleSet) = css { specific { handler() } }

fun StyledBuilder<*>.cssSnackbarColor(color: Color) = css {
	child(".MuiSnackbarContent-root") {
		backgroundColor = color
	}
}

fun StyledBuilder<*>.cssFlexBox(direction: FlexDirection? = null,
                                wrap: FlexWrap? = null,
                                justifyContent: JustifyContent? = null,
                                alignItems: Align? = null) = css {
	display = Display.flex
	direction?.let { this.flexDirection = it }
	wrap?.let { this.flexWrap = it }
	justifyContent?.let { this.justifyContent = it }
	alignItems?.let { this.alignItems = it }
}

fun StyledBuilder<*>.cssFlexItem(grow: Double? = null,
                                 shrink: Double? = null,
                                 basis: FlexBasis? = null,
                                 alignSelf: Align? = null) = css {
	grow?.let { this.flexGrow = it }
	shrink?.let { this.flexShrink = it }
	basis?.let { this.flexBasis = it }
	alignSelf?.let { this.alignSelf = it }
}

fun StyledBuilder<*>.cssPositionSticky(left: LinearDimension? = null,
                                       right: LinearDimension? = null,
                                       top: LinearDimension? = null,
                                       bottom: LinearDimension? = null) = css {
	position = Position.sticky
	left?.let { this.left = it }
	right?.let { this.right = it }
	top?.let { this.top = it }
	bottom?.let { this.bottom = it }
}
