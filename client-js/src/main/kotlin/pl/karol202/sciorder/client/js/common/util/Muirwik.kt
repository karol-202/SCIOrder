package pl.karol202.sciorder.client.js.common.util

import com.ccfraser.muirwik.components.MColor
import com.ccfraser.muirwik.components.createMuiThemeFunction
import com.ccfraser.muirwik.components.currentTheme
import com.ccfraser.muirwik.components.dialog.DialogMaxWidth
import com.ccfraser.muirwik.components.dialog.ModalOnCloseReason
import com.ccfraser.muirwik.components.dialog.mDialog
import com.ccfraser.muirwik.components.expansion.MExpansionPanelProps
import com.ccfraser.muirwik.components.mDivider
import com.ccfraser.muirwik.components.styles.Theme
import com.ccfraser.muirwik.components.styles.ThemeOptions
import kotlinext.js.assign
import kotlinx.css.*
import react.RBuilder
import react.RElementBuilder
import styled.css
import styled.styledDiv

object Muirwik
{
	val DIVIDER_COLOR = rgba(255, 255, 255, 0.2)
}

fun createMuiTheme(options: ThemeOptions) = createMuiThemeFunction(options).unsafeCast<Theme>()

fun RBuilder.themeTextField(textFieldColor: MColor,
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

				descendants("label.Mui-focused") { not(".Mui-error") { color = colorValue } }
				descendants(".MuiInput-underline:after") { borderBottomColor = colorValue }
			}
			handler()
		}

fun RBuilder.dialog(open: Boolean,
                    onClose: ((ModalOnCloseReason) -> Unit)? = null,
                    handler: RBuilder.() -> Unit) =
		mDialog(fullWidth = true,
		        maxWidth = DialogMaxWidth.sm,
		        open = open,
		        onClose = { _, reason -> onClose?.invoke(reason) }) {
			themeTextField(textFieldColor = MColor.secondary) { handler() }
		}

fun RBuilder.divider() = mDivider {
	overrideCss { backgroundColor = Muirwik.DIVIDER_COLOR }
}

var RElementBuilder<MExpansionPanelProps>.unmountOnExit
	get() = transitionProps.unmountOnExit as Boolean
	set(value) { transitionProps = assign(transitionProps) { this.unmountOnExit = value } }
	

private var RElementBuilder<MExpansionPanelProps>.transitionProps
	get() = attrs.asDynamic().TransitionProps
	set(value) { attrs.asDynamic().TransitionProps = value }
