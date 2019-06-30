plugins {
    id(Plugins.ANDROID_APP) version Versions.ANDROID_BUILD_TOOLS
    id(Plugins.KOTLIN_ANDROID) version Versions.KOTLIN
    id(Plugins.KOTLIN_KAPT) version Versions.KOTLIN
    id(Plugins.ANDROID_KTX) version Versions.KOTLIN
    id(Plugins.ANDROID_NAVIGATION) version Versions.ANDROID_NAVIGATION
}

android {
    compileSdkVersion(29)

    defaultConfig {
        applicationId = "pl.karol202.sciorder.client.android.admin"
        minSdkVersion(21)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes["release"].apply {
        isMinifyEnabled = false
        proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    packagingOptions {
        exclude("META-INF/kotlinx-coroutines-core.kotlin_module")
        exclude("META-INF/ktor-client-serialization.kotlin_module")
        exclude("META-INF/ktor-client-json.kotlin_module")
        exclude("META-INF/ktor-http.kotlin_module")
        exclude("META-INF/kotlinx-io.kotlin_module")
        exclude("META-INF/atomicfu.kotlin_module")
        exclude("META-INF/ktor-utils.kotlin_module")
        exclude("META-INF/kotlinx-coroutines-io.kotlin_module")
        exclude("META-INF/kotlinx-serialization-runtime.kotlin_module")
        exclude("META-INF/ktor-http-cio.kotlin_module")
        exclude("META-INF/ktor-client-core.kotlin_module")
    }
}

androidExtensions {
    isExperimental = true
}

dependencies {
    implementation(project(":client-android"))
}
