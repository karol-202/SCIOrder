import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id(Plugins.KOTLIN_JVM)
    id(Plugins.SHADOW)
    id(Plugins.APPLICATION)
}

application.mainClassName = "io.ktor.server.netty.EngineMain"

tasks {
    withType(KotlinCompile::class) {
        kotlinOptions.jvmTarget = "1.8"
        kotlinOptions.freeCompilerArgs += listOf("-Xopt-in=io.ktor.util.KtorExperimentalAPI")
    }

    withType(ShadowJar::class) {
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
    implementation(Deps.KTOR_AUTH)
    implementation(Deps.KTOR_AUTH_JWT)
    implementation(Deps.KTOR_SERIALIZATION)

    implementation(Deps.EXPOSED_CORE)
    implementation(Deps.EXPOSED_DAO)
    implementation(Deps.EXPOSED_JDBC)

    implementation(Deps.KOIN)
    implementation(Deps.KOIN_KTOR)
    
    implementation(Deps.JDBC_MYSQL)
    implementation(Deps.LOGBACK)
}
