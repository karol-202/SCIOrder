package pl.karol202.sciorder.server.routes.owner

import io.ktor.application.call
import io.ktor.routing.Route
import io.ktor.routing.get
import pl.karol202.sciorder.common.model.Owner
import pl.karol202.sciorder.server.util.badRequest
import pl.karol202.sciorder.server.util.notFound
import pl.karol202.sciorder.server.util.ok

fun Route.getOwner(ownerDao: OwnerDao) = get {
	val name = call.parameters["name"] ?: return@get badRequest()
	val hash = call.parameters["hash"]
	val owner = if(hash != null) ownerDao.getOwnerByNameAndHash(name, hash)
				else ownerDao.getOwnerByName(name)?.hideHash()
	if(owner != null) ok(owner)
	else notFound()
}

private fun Owner.hideHash() = copy(hash = "")
