
import org.jetbrains.kotlin.gradle.frontend.webpack.WebPackExtension
import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinJsDce

plugins {
    id(Plugins.KOTLIN_JS) version Versions.KOTLIN
    id(Plugins.KOTLIN_FRONTEND) version Versions.KOTLIN_FRONTEND
    id(Plugins.KOTLIN_DCE_JS) version Versions.KOTLIN
}

val devMode by project.booleanProperties

tasks {
    "compileKotlin2Js"(Kotlin2JsCompile::class) {
        kotlinOptions {
            moduleKind = "commonjs"
            sourceMap = devMode
            sourceMapEmbedSources = "always"
        }
    }

    "runDceKotlinJs"(KotlinJsDce::class) {
        dceOptions.devMode = devMode
    }
}

kotlinFrontend {
    sourceMaps = devMode

    npm {
        dependency(Deps.NPM_CORE_JS, Versions.CORE_JS)
        dependency(Deps.NPM_REACT, Versions.REACT)
        dependency(Deps.NPM_REACT_DOM, Versions.REACT)
        dependency(Deps.NPM_MATERIALUI_CORE, Versions.MATERIALUI_CORE)
        dependency(Deps.NPM_MATERIALUI_ICONS, Versions.MATERIALUI_ICONS)
        dependency(Deps.NPM_STYLED_COMPONENTS, Versions.STYLED_COMPONENTS)
        dependency(Deps.NPM_INLINE_STYLE_PREFIXER, Versions.INLINE_STYLE_PREFIXER)
    }

    bundle("webpack", delegateClosureOf<WebPackExtension> {
        bundleName = "sciorder"
        sourceMapEnabled = devMode
        contentPath = file("src/main/web/")
        publicPath = "/"
        host = "localhost"
        port = 8080
        mode = if(devMode) "development" else "production"
    })
}

dependencies {
    implementation(project(":client"))

    implementation(Deps.KTOR_CLIENT_ENGINE_JS)

    implementation(Deps.EXTENSIONS_JS)
    implementation(Deps.REACT_KOTLIN)
    implementation(Deps.REACT_DOM_KOTLIN)
    implementation(Deps.STYLED_KOTLIN)
    implementation(Deps.MUIRWIK)
}
