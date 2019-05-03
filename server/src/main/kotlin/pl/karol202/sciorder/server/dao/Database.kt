package pl.karol202.sciorder.server.dao

import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

object Database
{
	private val client = KMongo.createClient().coroutine
	@PublishedApi internal val database = client.getDatabase("sciorder")

	inline fun <reified C : Any> getCollection() = database.getCollection<C>()
}
