package pl.karol202.sciorder.client.js.common.view

import com.ccfraser.muirwik.components.*
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.display
import kotlinx.css.flexDirection
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import styled.css
import styled.styledDiv

class MainView : RComponent<RProps, RState>()
{
	override fun RBuilder.render()
	{
		styledDiv {
			css {
				display = Display.flex
				flexDirection = FlexDirection.column
			}

			mAppBar(position = MAppBarPosition.relative) {
				mToolbar {
					mToolbarTitle("SCIOrder")
				}
			}

			styledDiv {
				mButton("Hello world!")
			}
		}
	}
}

fun RBuilder.mainView() = child(MainView::class) { }
