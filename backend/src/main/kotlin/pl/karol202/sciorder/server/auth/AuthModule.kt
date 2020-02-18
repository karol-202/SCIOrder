package pl.karol202.sciorder.server.auth

import org.koin.dsl.module
import pl.karol202.sciorder.server.config.Config
import pl.karol202.sciorder.server.config.getProperty

fun authModule() = module {
	single { JWTProvider(realm = getProperty(Config.JWT_REALM),
	                     secret = getProperty(Config.JWT_SECRET)) }
}
