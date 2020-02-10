package pl.karol202.sciorder.server.database

import com.mongodb.client.model.IndexOptions
import org.litote.kmongo.ascendingIndex
import org.litote.kmongo.setValue
import pl.karol202.sciorder.common.model.Owner

class DatabaseOwnerDao(database: KMongoDatabase): OwnerDao
{
    private val ownersCollection = database.getCollection<Owner>()

	override suspend fun insertOwner(owner: Owner): Boolean
	{
		ownersCollection.createIndex(ascendingIndex(Owner::name), IndexOptions().unique(true))
		return try
		{
			ownersCollection.insertOne(owner)
			true
		}
		catch(e: Exception)
		{
			e.printStackTrace()
			false
		}
	}

	override suspend fun updateOwnerHash(id: String, hash: String) =
			ownersCollection.updateOne(Owner::_id eq id, setValue(Owner::hash, hash))
					.let { it.wasAcknowledged() && it.matchedCount > 0 }

	override suspend fun getOwnerById(id: String) =
			ownersCollection.findOneById(id)

	override suspend fun getOwnerByName(name: String) =
			ownersCollection.findOne(Owner::name eq name)

	override suspend fun getOwnerByNameAndHash(name: String, hash: String) =
			ownersCollection.findOne(Owner::name eq name, Owner::hash eq hash)
}
