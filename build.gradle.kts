plugins {
    id(Plugins.KOTLIN_JVM) version Versions.KOTLIN apply false
    id(Plugins.KOTLIN_MULTIPLATFORM) version Versions.KOTLIN apply false
    id(Plugins.KOTLIN_KAPT) version Versions.KOTLIN apply false
    id(Plugins.KOTLINX_SERIALIZATION) version Versions.KOTLIN apply false
    id(Plugins.ANDROID_LIBRARY) version Versions.ANDROID_BUILD_TOOLS apply false
    id(Plugins.ANDROID_KTX) version Versions.KOTLIN apply false
    id(Plugins.ANDROID_NAVIGATION) version Versions.ANDROID_NAVIGATION apply false
    id(Plugins.KOTLIN_DCE_JS) version Versions.KOTLIN apply false
    id(Plugins.KOTLIN_FRONTEND) version Versions.KOTLIN_FRONTEND apply false
    id(Plugins.SHADOW) version Versions.SHADOW apply false
}

allprojects {
    repositories {
        google()
        jcenter()
        maven("https://kotlin.bintray.com/kotlin-js-wrappers/")
        maven("https://jitpack.io")
    }
}
