package pl.karol202.sciorder.server.database

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.experimental.transaction
import java.sql.Connection

object AppDatabase
{
	val db = Database.connect("jdbc:sqlite:app.db", "org.sqlite.JDBC")

	init
	{
		TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
		//createTable()
	}

	//private fun createTable() = runBlocking { Orders.createTable(db) }
}

suspend fun <T> query(block: () -> T) = withContext(Dispatchers.IO) {
	transaction { block() }
}

suspend fun <T> query(database: Database, block: () -> T) = withContext(Dispatchers.IO) {
	transaction(database) { block() }
}