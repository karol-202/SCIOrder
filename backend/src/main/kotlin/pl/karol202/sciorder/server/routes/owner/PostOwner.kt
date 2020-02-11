package pl.karol202.sciorder.server.routes.owner

import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.routing.Route
import io.ktor.routing.post
import pl.karol202.sciorder.common.model.Owner
import pl.karol202.sciorder.server.util.badRequest
import pl.karol202.sciorder.server.util.conflict
import pl.karol202.sciorder.server.util.created

fun Route.postOwner(ownerDao: OwnerDao) = post {
	val owner = call.receive<Owner>().overwrite()
	if(!owner.isValid) return@post badRequest()
	val success = ownerDao.insertOwner(owner)
	if(success) created(owner) else conflict()
}

private fun Owner.overwrite() = copy(_id = newStringId<Owner>())
