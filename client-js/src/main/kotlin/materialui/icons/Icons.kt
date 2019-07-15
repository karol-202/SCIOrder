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
