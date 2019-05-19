package pl.karol202.sciorder.server.database

import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

class KMongoDatabase(connectionUri: String)
{
	private val client = KMongo.createClient(connectionUri).coroutine
	@PublishedApi internal val database = client.getDatabase("sciorder")

	inline fun <reified C : Any> getCollection() = database.getCollection<C>()
}
