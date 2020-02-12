package pl.karol202.sciorder.server.config

import io.ktor.application.ApplicationEnvironment
import org.koin.core.KoinApplication
import org.koin.core.scope.Scope

enum class Config(val path: String)
{
	JWT_REALM_ADMIN("jwt.realm.admin"),
	JWT_REALM_USER("jwt.realm.user"),
	JWT_SECRET("jwt.secret"),
	ARG_DB("database.uri");
	
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
