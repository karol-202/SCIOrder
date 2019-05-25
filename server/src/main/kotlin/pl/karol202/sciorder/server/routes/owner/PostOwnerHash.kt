package pl.karol202.sciorder.server.routes.owner

import io.ktor.application.call
import io.ktor.routing.Route
import io.ktor.routing.post
import pl.karol202.sciorder.server.database.OwnerDao
import pl.karol202.sciorder.server.util.badRequest
import pl.karol202.sciorder.server.util.notFound
import pl.karol202.sciorder.server.util.ok

fun Route.postOwnerHash(ownerDao: OwnerDao) = post {
	val ownerId = call.parameters["ownerId"] ?: return@post badRequest()
	val hash = call.parameters["hash"] ?: return@post badRequest()
	val success = ownerDao.updateOwnerHash(ownerId, hash)
	if(success) ok() else notFound()
}
