package pl.karol202.sciorder.server.database

import pl.karol202.sciorder.common.model.Owner

interface OwnerDao
{
	suspend fun insertOwner(owner: Owner): Boolean

	suspend fun updateOwnerHash(id: String, hash: String): Boolean

	suspend fun getOwnerById(id: String): Owner?

	suspend fun getOwnerByName(name: String): Owner?

	suspend fun getOwnerByNameAndHash(name: String, hash: String): Owner?
}
