rootProject.name = "sciorder"

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        //maven("https://dl.bintray.com/kotlin/kotlin-eap")
    }
    resolutionStrategy {
        eachPlugin {
            // Android does not place its plugins into Gradle repo,
            // so their search location must be specified
            // in order to be able to use them with plugins {} dsl (without buildscript block)
            when(requested.id.id)
            {
                "com.android.application", "com.android.library" -> useModule("com.android.tools.build:gradle:${requested.version}")
                //"kotlin2js", "kotlin-dce-js" -> useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:${requested.version}")
                //"org.jetbrains.kotlin.frontend" -> useModule("org.jetbrains.kotlin:kotlin-frontend-plugin:${requested.version}")
                //"kotlinx-serialization" -> useModule("org.jetbrains.kotlin:kotlin-serialization:${requested.version}")
                "androidx.navigation.safeargs.kotlin" -> useModule("androidx.navigation:navigation-safe-args-gradle-plugin:${requested.version}")
            }
        }
    }
}

include(":common")
include(":server")
include(":client")
//include(":client-js")

val includeAndroid: String by settings
if(includeAndroid.toBoolean()) {
    include(":client-android")
    include(":client-android-admin")
    include(":client-android-user")
}
