import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id(Plugins.KOTLIN_JVM) version Versions.KOTLIN
    id(Plugins.SHADOW) version Versions.SHADOW
    id(Plugins.APPLICATION)
}

application.mainClassName = "io.ktor.server.netty.EngineMain"

tasks {
    "compileKotlin"(KotlinCompile::class) {
        kotlinOptions.jvmTarget = "1.8"
    }

    "shadowJar"(ShadowJar::class) {
        manifest.attributes("Main-Class" to application.mainClassName)
    }

    register("stage") {
        dependsOn("clean", "shadowJar")
    }
}

dependencies {
    implementation(project(":common"))
    implementation(Deps.STDLIB_JVM)

    implementation(Deps.KTOR_SERVER_NETTY)
    implementation(Deps.KTOR_GSON)

    implementation(Deps.KMONGO)
    implementation(Deps.KMONGO_ASYNC)
    implementation(Deps.KMONGO_COROUTINE)

    implementation(Deps.KOIN)
    implementation(Deps.KOIN_KTOR)

    implementation(Deps.LOGBACK)
}
