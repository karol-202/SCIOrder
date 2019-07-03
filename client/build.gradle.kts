import com.android.build.gradle.LibraryExtension

plugins {
    id(Plugins.ANDROID_LIBRARY) version Versions.ANDROID_BUILD_TOOLS apply false
    id(Plugins.KOTLIN_MULTIPLATFORM) version Versions.KOTLIN
    id(Plugins.KOTLINX_SERIALIZATION) version Versions.KOTLIN
}

val includeAndroid by project.booleanProperties

if(includeAndroid) {
    apply(plugin = Plugins.ANDROID_LIBRARY)

    // Only way to configure android block when applying plugin in legacy way
    configure<LibraryExtension> {
        compileSdkVersion(29)

        defaultConfig {
            minSdkVersion(21)
            targetSdkVersion(29)
            versionCode = 1
            versionName = "1.0"
        }

        buildTypes["release"].apply {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

        sourceSets["main"].setRoot("src/androidMain")

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }
    }
}

kotlin {
    if(includeAndroid) android()
    js {
        compilations["main"].kotlinOptions {
            sourceMap = true
            moduleKind = "commonjs"
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                api(project(":common"))
                api(Deps.COROUTINES_CORE_COMMON)

                implementation(Deps.KTOR_CLIENT_CORE_COMMON)
                implementation(Deps.KTOR_CLIENT_JSON_COMMON)
                implementation(Deps.KTOR_CLIENT_SERIALIZATION_COMMON)
            }
        }

        if(includeAndroid) getByName("androidMain").dependencies {
            api(Deps.COROUTINES_CORE_JVM)

            implementation(Deps.KTOR_CLIENT_CORE_JVM)
            implementation(Deps.KTOR_CLIENT_JSON_JVM)
            implementation(Deps.KTOR_CLIENT_SERIALIZATION_JVM)

            implementation(Deps.ANDROID_VIEWMODEL)
        }

        js().compilations["main"].dependencies {
            api(Deps.COROUTINES_CORE_JS)

            implementation(Deps.KTOR_CLIENT_CORE_JS)
            implementation(Deps.KTOR_CLIENT_JSON_JS)
            implementation(Deps.KTOR_CLIENT_SERIALIZATION_JS)

            implementation(npm(Deps.SHA1, Versions.SHA1))
        }
    }
}
