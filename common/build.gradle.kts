plugins {
    // Adding Android to classpath surprisingly removes error related to kotlinNodeJs extension
    id(Plugins.ANDROID_LIBRARY) version Versions.ANDROID_BUILD_TOOLS apply false
    id(Plugins.KOTLIN_MULTIPLATFORM) version Versions.KOTLIN
	id(Plugins.KOTLINX_SERIALIZATION) version Versions.KOTLIN
}

kotlin {
    jvm()
    js {
        compilations["main"].kotlinOptions {
            sourceMap = true
            moduleKind = "commonjs"
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                api(Deps.STDLIB_COMMON)
                api(Deps.SERIALIZATION_COMMON)
            }
        }

        jvm().compilations["main"].dependencies {
            api(Deps.SERIALIZATION_JVM)
        }

        js().compilations["main"].dependencies {
            api(Deps.SERIALIZATION_JS)
        }
    }
}
