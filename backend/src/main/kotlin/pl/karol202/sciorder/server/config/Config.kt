package pl.karol202.sciorder.server.config

import io.ktor.application.ApplicationEnvironment
import org.koin.core.KoinApplication
import org.koin.core.scope.Scope

enum class Config(val path: String)
{
	JWT_REALM("jwt.realm"),
	JWT_SECRET("jwt.secret"),
	DB_URI("database.uri"),
	DB_DRIVER("database.driver"),
	DB_USER("database.user"),
	DB_PASSWORD("database.password");
	
	companion object
	{
		val paths = values().map { it.path }
	}
}

fun KoinApplication.propertiesFromConfig(applicationEnvironment: ApplicationEnvironment) =
		properties(Config.paths.mapNotNull { name ->
			applicationEnvironment.config.propertyOrNull(name)?.getString()?.let { name to it }
		}.toMap())

fun <T> Scope.getProperty(config: Config) = getProperty<T>(config.path)
