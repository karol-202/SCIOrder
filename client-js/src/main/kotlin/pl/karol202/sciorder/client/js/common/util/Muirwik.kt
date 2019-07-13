package pl.karol202.sciorder.client.js.common.util

import com.ccfraser.muirwik.components.MColor
import com.ccfraser.muirwik.components.createMuiThemeFunction
import com.ccfraser.muirwik.components.currentTheme
import com.ccfraser.muirwik.components.mDivider
import com.ccfraser.muirwik.components.styles.Theme
import com.ccfraser.muirwik.components.styles.ThemeOptions
import kotlinx.css.Color
import kotlinx.css.backgroundColor
import kotlinx.css.borderBottomColor
import kotlinx.css.color
import kotlinx.css.height
import kotlinx.css.pct
import kotlinx.css.rgba
import react.RBuilder
import styled.css
import styled.styledDiv

object Muirwik
{
	val DIVIDER_COLOR = rgba(255, 255, 255, 0.2)
}

fun createMuiTheme(options: ThemeOptions) = createMuiThemeFunction(options).unsafeCast<Theme>()

fun RBuilder.textFieldColor(textFieldColor: MColor,
                            handler: RBuilder.() -> Unit) =
		styledDiv {
			css {
				val colorValue = Color(when(textFieldColor)
				{
					MColor.default -> return@css
					MColor.inherit -> "inherit"
					MColor.primary -> currentTheme.palette.primary.main
					MColor.secondary -> currentTheme.palette.secondary.main
				})

				descendants("label.Mui-focused") { color = colorValue }
				descendants(".MuiInput-underline:after") { borderBottomColor = colorValue }
				
				height = 100.pct
			}
			handler()
		}

fun RBuilder.divider() = mDivider {
	css {
		specific { backgroundColor = Muirwik.DIVIDER_COLOR }
	}
}
