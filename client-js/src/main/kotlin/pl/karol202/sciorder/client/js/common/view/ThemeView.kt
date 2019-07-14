package pl.karol202.sciorder.client.js.common.view

import com.ccfraser.muirwik.components.Colors
import com.ccfraser.muirwik.components.MColor
import com.ccfraser.muirwik.components.currentTheme
import com.ccfraser.muirwik.components.mCssBaseline
import com.ccfraser.muirwik.components.mMuiThemeProvider
import com.ccfraser.muirwik.components.styles.PaletteColor
import com.ccfraser.muirwik.components.styles.Theme
import com.ccfraser.muirwik.components.styles.ThemeOptions
import kotlinext.js.jsObject
import pl.karol202.sciorder.client.js.common.util.createMuiTheme
import pl.karol202.sciorder.client.js.common.util.themeTextField
import react.RBuilder
import react.RProps
import react.RState

class ThemeComponent : View<RProps, RState>()
{
	init
	{
		currentTheme = createTheme()
	}

	private fun createTheme(): Theme
	{
		val themeOptions: ThemeOptions = jsObject {
			palette = jsObject {
				type = "dark"
				primary = jsObject<PaletteColor> {
					main = Colors.Red.shade500.toString()
				}
				secondary = jsObject<PaletteColor> {
					main = Colors.Green.accent400.toString()
				}
			}
		}
		return createMuiTheme(themeOptions)
	}

	override fun RBuilder.render()
	{
		mMuiThemeProvider(currentTheme) {
			mCssBaseline()
			themeTextField(textFieldColor = MColor.secondary) {
				children()
			}
		}
	}
}

fun RBuilder.themeComponent(handler: RBuilder.() -> Unit) = child(ThemeComponent::class) { handler() }
