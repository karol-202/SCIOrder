package pl.karol202.sciorder.client.js.common.util

import com.ccfraser.muirwik.components.MColor
import com.ccfraser.muirwik.components.currentTheme
import kotlinx.css.Color
import kotlinx.css.borderBottomColor
import kotlinx.css.color
import react.RBuilder
import styled.css
import styled.styledDiv

fun RBuilder.mTextFieldColor(textFieldColor: MColor,
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
			}
			handler()
		}
