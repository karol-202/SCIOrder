plugins {
    // Specifying plugins (and theirs versions) in the root build.gradle without applying
    // them makes them present in classpath, so in the rest of build.gradles versions can be skipped.
    id(Plugins.KOTLIN_MULTIPLATFORM) version Versions.KOTLIN apply false
    id(Plugins.KOTLIN_JVM) version Versions.KOTLIN apply false
    id(Plugins.KOTLIN_JS) version Versions.KOTLIN apply false
    id(Plugins.KOTLIN_ANDROID) version Versions.KOTLIN apply false
    id(Plugins.KOTLIN_ANDROID_EXTENSIONS) version Versions.KOTLIN apply false
    id(Plugins.KOTLIN_SERIALIZATION) version Versions.KOTLIN apply false
    id(Plugins.KOTLIN_KAPT) version Versions.KOTLIN apply false
    id(Plugins.ANDROID_APP) version Versions.ANDROID_GRADLE_PLUGIN apply false
    id(Plugins.ANDROID_LIBRARY) version Versions.ANDROID_GRADLE_PLUGIN apply false
    id(Plugins.ANDROID_NAVIGATION) version Versions.ANDROID_NAVIGATION apply false
    id(Plugins.SHADOW) version Versions.SHADOW apply false
}

allprojects {
    repositories {
        google()
        jcenter()
        //maven("https://kotlin.bintray.com/kotlin-js-wrappers/")
        maven("https://jitpack.io")
    }
}
