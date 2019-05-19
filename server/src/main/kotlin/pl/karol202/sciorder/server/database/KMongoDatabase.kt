package pl.karol202.sciorder.server.database

import com.mongodb.ConnectionString
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

class KMongoDatabase(connectionUri: String)
{
	private val connectionString = ConnectionString(connectionUri)
	private val databaseName = connectionString.database ?: throw IllegalArgumentException("No database specified")

	private val client = KMongo.createClient(connectionString).coroutine
	@PublishedApi internal val database = client.getDatabase(databaseName)

	inline fun <reified C : Any> getCollection() = database.getCollection<C>()
}
