package pl.karol202.sciorder.client.js.common.component

import pl.karol202.sciorder.client.js.common.view.mainView
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState

class AppComponent : RComponent<RProps, RState>()
{
	override fun RBuilder.render()
	{
		themeComponent {
			mainView()
		}
	}
}

fun RBuilder.appComponent() = child(AppComponent::class) { }
