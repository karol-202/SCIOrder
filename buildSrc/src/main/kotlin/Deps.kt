object Deps
{
	// Common
	const val STDLIB_COMMON = "org.jetbrains.kotlin:kotlin-stdlib-common:${Versions.KOTLIN}"
	const val STDLIB_JVM = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.KOTLIN}"

	const val SERIALIZATION_COMMON = "org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:${Versions.KOTLINX_SERIALIZATION}"
	const val SERIALIZATION_JVM = "org.jetbrains.kotlinx:kotlinx-serialization-runtime:${Versions.KOTLINX_SERIALIZATION}"
	const val SERIALIZATION_JS = "org.jetbrains.kotlinx:kotlinx-serialization-runtime-js:${Versions.KOTLINX_SERIALIZATION}"

	const val KOIN = "org.koin:koin-core:${Versions.KOIN}"

	// Client
	const val COROUTINES_CORE_COMMON = "org.jetbrains.kotlinx:kotlinx-coroutines-core-common:${Versions.COROUTINES}"
	const val COROUTINES_CORE_ANDROID = "org.jetbrains.kotlinx:kotlinx-coroutines-core-android:${Versions.COROUTINES}"
	const val COROUTINES_CORE_JS = "org.jetbrains.kotlinx:kotlinx-coroutines-core-js:${Versions.COROUTINES}"

	const val KTOR_CLIENT_CORE_COMMON = "io.ktor:ktor-client-core:${Versions.KTOR}"
	const val KTOR_CLIENT_CORE_JVM = "io.ktor:ktor-client-core-jvm:${Versions.KTOR}"
	const val KTOR_CLIENT_CORE_JS = "io.ktor:ktor-client-core-js:${Versions.KTOR}"
	const val KTOR_CLIENT_JSON_COMMON = "io.ktor:ktor-client-json:${Versions.KTOR}"
	const val KTOR_CLIENT_JSON_JVM = "io.ktor:ktor-client-json-jvm:${Versions.KTOR}"
	const val KTOR_CLIENT_JSON_JS = "io.ktor:ktor-client-json-js:${Versions.KTOR}"
	const val KTOR_CLIENT_SERIALIZATION_COMMON = "io.ktor:ktor-client-serialization:${Versions.KTOR}"
	const val KTOR_CLIENT_SERIALIZATION_JVM = "io.ktor:ktor-client-serialization-jvm:${Versions.KTOR}"
	const val KTOR_CLIENT_SERIALIZATION_JS = "io.ktor:ktor-client-serialization-js:${Versions.KTOR}"
	const val KTOR_CLIENT_ENGINE_OKHTTP = "io.ktor:ktor-client-okhttp:${Versions.KTOR}"
	const val KTOR_CLIENT_ENGINE_JS = "io.ktor:ktor-client-js:${Versions.KTOR}"

	// Android
	const val KOIN_ANDROID = "org.koin:koin-android:${Versions.KOIN}"
	const val KOIN_ANDROID_VIEWMODEL = "org.koin:koin-androidx-viewmodel:${Versions.KOIN}"

	const val ANDROID_KTX = "androidx.core:core-ktx:${Versions.ANDROID_KTX}"
	const val ANDROID_APPCOMPAT = "androidx.appcompat:appcompat:${Versions.ANDROID_APPCOMPAT}"
	const val ANDROID_FRAGMENT = "androidx.fragment:fragment-ktx:${Versions.ANDROID_FRAGMENT}"
	const val ANDROID_LIFECYCLE_VIEWMODEL = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.ANDROID_VIEWMODEL}"
	const val ANDROID_CONSTRAINT_LAYOUT = "androidx.constraintlayout:constraintlayout:${Versions.ANDROID_CONSTRAINT_LAYOUT}"
	const val ANDROID_RECYCLER_VIEW = "androidx.recyclerview:recyclerview:${Versions.ANDROID_RECYCLER_VIEW}"
	const val ANDROID_PREFERENCE = "androidx.preference:preference-ktx:${Versions.ANDROID_PREFERENCE}"
	const val ANDROID_NAVIGATION_FRAGMENT = "androidx.navigation:navigation-fragment-ktx:${Versions.ANDROID_NAVIGATION}"
	const val ANDROID_NAVIGATION_UI = "androidx.navigation:navigation-ui-ktx:${Versions.ANDROID_NAVIGATION}"
	const val ANDROID_MATERIAL = "com.google.android.material:material:${Versions.ANDROID_MATERIAL}"

	const val ANDROID_ROOM_COMPILER = "androidx.room:room-compiler:${Versions.ANDROID_ROOM}"
	const val ANDROID_ROOM = "androidx.room:room-runtime:${Versions.ANDROID_ROOM}"
	const val ANDROID_ROOM_KTX = "androidx.room:room-ktx:${Versions.ANDROID_ROOM}"

	// JS
	const val NPM_CORE_JS = "core-js"
	const val NPM_REACT = "react"
	const val NPM_REACT_DOM = "react-dom"
	const val NPM_REACT_ROUTER_DOM = "react-router-dom"
	const val NPM_MATERIALUI_CORE = "@material-ui/core"
	const val NPM_MATERIALUI_ICONS = "@material-ui/icons"
	const val NPM_STYLED_COMPONENTS = "styled-components"
	const val NPM_INLINE_STYLE_PREFIXER = "inline-style-prefixer"

	const val REACT_KOTLIN = "org.jetbrains:kotlin-react:${Versions.REACT_KOTLIN}"
	const val REACT_DOM_KOTLIN = "org.jetbrains:kotlin-react-dom:${Versions.REACT_KOTLIN}"
	const val REACT_ROUTER_DOM_KOTLIN = "org.jetbrains:kotlin-react-router-dom:${Versions.REACT_ROUTER_KOTLIN}"
	const val STYLED_KOTLIN = "org.jetbrains:kotlin-styled:${Versions.STYLED_KOTLIN}"
	const val EXTENSIONS_JS = "org.jetbrains:kotlin-extensions:${Versions.EXTENSIONS_JS}"
	//const val MUIRWIK = "com.ccfraser.muirwik:muirwik-components:${Versions.MUIRWIK}"
	const val MUIRWIK = "com.github.karol-202:muirwik:e1f9ea0dd5"
	const val TEXT_ENCODING = "text-encoding"
	const val SHA1 = "sha1"

	// Server
	const val KTOR_SERVER_NETTY = "io.ktor:ktor-server-netty:${Versions.KTOR}"
	const val KTOR_GSON = "io.ktor:ktor-gson:${Versions.KTOR}"

	const val KOIN_KTOR = "org.koin:koin-ktor:${Versions.KOIN}"

	const val KMONGO = "org.litote.kmongo:kmongo:${Versions.KMONGO}"
	const val KMONGO_ASYNC = "org.litote.kmongo:kmongo-async:${Versions.KMONGO}"
	const val KMONGO_COROUTINE = "org.litote.kmongo:kmongo-coroutine:${Versions.KMONGO}"

	const val LOGBACK = "ch.qos.logback:logback-classic:${Versions.LOGBACK}"
}
