@file:Suppress("UnsafeCastFromDynamic")
package materialui.icons

import react.RBuilder
import react.RClass
import react.RProps

@JsModule("@material-ui/icons/Visibility")
private external val visibilityModule: dynamic
private val visibilityClass: RClass<RProps> = visibilityModule.default
fun RBuilder.iconVisibility() = visibilityClass { }

@JsModule("@material-ui/icons/VisibilityOff")
private external val visibilityOffModule: dynamic
private val visibilityOffClass: RClass<RProps> = visibilityOffModule.default
fun RBuilder.iconVisibilityOff() = visibilityOffClass { }

@JsModule("@material-ui/icons/ExitToApp")
private external val logoutModule: dynamic
private val logoutClass: RClass<RProps> = logoutModule.default
fun RBuilder.iconLogout() = logoutClass { }

@JsModule("@material-ui/icons/Clear")
private external val clearModule: dynamic
private val clearClass: RClass<RProps> = clearModule.default
fun RBuilder.iconClear() = clearClass { }

@JsModule("@material-ui/icons/Create")
private external val createModule: dynamic
private val createClass: RClass<RProps> = createModule.default
fun RBuilder.iconEdit() = createClass { }

@JsModule("@material-ui/icons/KeyboardArrowLeft")
private external val keyboardArrowLeftModule: dynamic
private val keyboardArrowLeftClass: RClass<RProps> = keyboardArrowLeftModule.default
fun RBuilder.iconArrowLeft() = keyboardArrowLeftClass { }

@JsModule("@material-ui/icons/KeyboardArrowRight")
private external val keyboardArrowRightModule: dynamic
private val keyboardArrowRightClass: RClass<RProps> = keyboardArrowRightModule.default
fun RBuilder.iconArrowRight() = keyboardArrowRightClass { }

@JsModule("@material-ui/icons/Refresh")
private external val refreshModule: dynamic
private val refreshClass: RClass<RProps> = refreshModule.default
fun RBuilder.iconRefresh() = refreshClass { }

@JsModule("@material-ui/icons/Delete")
private external val deleteModule: dynamic
private val deleteClass: RClass<RProps> = deleteModule.default
fun RBuilder.iconDelete() = deleteClass { }
