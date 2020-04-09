plugins {
    id(Plugins.ANDROID_APP)
    id(Plugins.KOTLIN_ANDROID)
    id(Plugins.KOTLIN_KAPT)
    id(Plugins.KOTLIN_ANDROID_EXTENSIONS)
    id(Plugins.ANDROID_NAVIGATION)
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
    
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
        kotlinOptions.freeCompilerArgs += listOf("-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                                                 "-Xopt-in=kotlinx.coroutines.FlowPreview",
                                                 "-Xopt-in=kotlinx.serialization.UnstableDefault")
    }
    
    packagingOptions {
        exclude("META-INF/*.kotlin_module")
    }
}

androidExtensions {
    isExperimental = true
}

dependencies {
    implementation(project(":client-android"))
}
