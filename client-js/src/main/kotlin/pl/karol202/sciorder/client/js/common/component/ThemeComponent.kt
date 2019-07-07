package pl.karol202.sciorder.client.js.common.component

import com.ccfraser.muirwik.components.*
import com.ccfraser.muirwik.components.styles.PaletteColor
import com.ccfraser.muirwik.components.styles.Theme
import com.ccfraser.muirwik.components.styles.ThemeOptions
import kotlinext.js.jsObject
import pl.karol202.sciorder.client.js.common.util.ExtendedComponent
import pl.karol202.sciorder.client.js.common.util.mTextFieldColor
import react.RBuilder
import react.RProps
import react.RState

class ThemeComponent : ExtendedComponent<RProps, RState>()
{
	override fun RState.init()
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
		return createMuiThemeFunction(themeOptions)
	}

	override fun RBuilder.render()
	{
		mMuiThemeProvider(currentTheme) {
			mCssBaseline()
			mTextFieldColor(textFieldColor = MColor.secondary) {
				children()
			}
		}
	}
}

fun RBuilder.themeComponent(handler: RBuilder.() -> Unit) = child(ThemeComponent::class) { handler() }
