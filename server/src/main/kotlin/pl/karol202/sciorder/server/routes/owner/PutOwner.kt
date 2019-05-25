package pl.karol202.sciorder.server.routes.owner

import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.routing.Route
import io.ktor.routing.put
import pl.karol202.sciorder.common.model.Owner
import pl.karol202.sciorder.server.database.OwnerDao
import pl.karol202.sciorder.server.util.created
import pl.karol202.sciorder.server.util.newStringId

fun Route.putOwner(ownerDao: OwnerDao) = put {
	val owner = call.receive<Owner>().override()
	ownerDao.insertOwner(owner)
	created(owner)
}

private fun Owner.override() = copy(_id = newStringId<Owner>())
