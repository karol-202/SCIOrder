plugins {
    id(Plugins.KOTLIN_MULTIPLATFORM)
	id(Plugins.KOTLIN_SERIALIZATION)
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
