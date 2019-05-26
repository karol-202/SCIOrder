package pl.karol202.sciorder.server.routes.owner

import io.ktor.application.call
import io.ktor.routing.Route
import io.ktor.routing.put
import pl.karol202.sciorder.server.database.OwnerDao
import pl.karol202.sciorder.server.util.badRequest
import pl.karol202.sciorder.server.util.notFound
import pl.karol202.sciorder.server.util.ok

fun Route.putOwnerHash(ownerDao: OwnerDao) = put {
	val ownerId = call.parameters["ownerId"] ?: return@put badRequest()
	val hash = call.parameters["hash"] ?: return@put badRequest()
	val success = ownerDao.updateOwnerHash(ownerId, hash)
	if(success) ok() else notFound()
}
