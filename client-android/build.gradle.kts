plugins {
    id(Plugins.ANDROID_LIBRARY)
    id(Plugins.KOTLIN_ANDROID)
	id(Plugins.KOTLIN_ANDROID_EXTENSIONS)
	id(Plugins.KOTLIN_KAPT)
}

android {
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

androidExtensions {
    isExperimental = true
}

dependencies {
    api(project(":client"))
    implementation(Deps.COROUTINES_ANDROID)

    implementation(Deps.KTOR_CLIENT_ENGINE_OKHTTP)

    api(Deps.KOIN)
    api(Deps.KOIN_ANDROID)
    api(Deps.KOIN_ANDROID_VIEWMODEL)

    // Android
    api(Deps.ANDROID_KTX)
    api(Deps.ANDROID_APPCOMPAT)
    api(Deps.ANDROID_FRAGMENT)
	api(Deps.ANDROID_LIFECYCLE_VIEWMODEL)
    api(Deps.ANDROID_CONSTRAINT_LAYOUT)
    api(Deps.ANDROID_RECYCLER_VIEW)
    api(Deps.ANDROID_PREFERENCE)
    api(Deps.ANDROID_NAVIGATION_FRAGMENT)
    api(Deps.ANDROID_NAVIGATION_UI)
    api(Deps.ANDROID_MATERIAL)

    kapt(Deps.ANDROID_ROOM_COMPILER)
    implementation(Deps.ANDROID_ROOM)
    implementation(Deps.ANDROID_ROOM_KTX)
}
