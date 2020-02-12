package pl.karol202.sciorder.server.auth

import org.koin.dsl.module
import pl.karol202.sciorder.server.config.Config
import pl.karol202.sciorder.server.config.getProperty

fun authModule() = module {
	single { JWTProvider(realmAdmin = getProperty(Config.JWT_REALM_ADMIN),
	                     realmUser = getProperty(Config.JWT_REALM_USER),
	                     secret = getProperty(Config.JWT_SECRET)) }
}
