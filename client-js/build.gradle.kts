
import org.jetbrains.kotlin.gradle.frontend.webpack.WebPackExtension
import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile

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

    "assemble" {
        dependsOn("runDceKotlinJs")
    }
}

kotlinFrontend {
    sourceMaps = devMode

    npm {
        dependency("core-js", Versions.CORE_JS)
        dependency("react", Versions.REACT)
        dependency("react-dom", Versions.REACT)
        dependency("@material-ui/core", Versions.MATERIALUI_CORE)
        dependency("@material-ui/icons", Versions.MATERIALUI_ICONS)
        dependency("inline-style-prefixer", Versions.INLINE_STYLE_PREFIXER)
        dependency("styled-components", Versions.STYLED_COMPONENTS)
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
