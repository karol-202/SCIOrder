package pl.karol202.sciorder.server.util

import io.ktor.application.ApplicationEnvironment
import io.ktor.util.KtorExperimentalAPI
import org.koin.core.KoinApplication

@KtorExperimentalAPI
fun KoinApplication.propertiesByKtorEnvironment(applicationEnvironment: ApplicationEnvironment,
                                                vararg names: String) =
		properties(names.mapNotNull { name ->
			applicationEnvironment.config.propertyOrNull(name)?.getString()?.let { name to it }
		}.toMap())
