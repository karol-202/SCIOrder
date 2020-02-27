package pl.karol202.sciorder.server.service.transaction

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import pl.karol202.sciorder.server.table.*

class TransactionServiceImpl(private val dbUri: String,
                             private val dbDriver: String,
                             private val dbUser: String,
                             private val dbPassword: String) : TransactionService
{
	init
	{
		connectToDatabase()
		createTables()
	}
	
	private fun connectToDatabase() = Database.connect(url = dbUri, driver = dbDriver, user = dbUser, password = dbPassword)
	
	private fun createTables() = runBlockingTransaction {
		SchemaUtils.create(Admins, Users, Stores, AdminStoreJoins, Products, ProductParameters, ProductParameterEnumValues, Orders,
		                   OrderEntries, OrderEntryParameterValues)
	}
	
	override suspend fun <T> runTransaction(block: suspend Transaction.() -> T) = runSuspendingTransaction(block)
	
	private suspend fun <T> runSuspendingTransaction(block: suspend Transaction.() -> T) =
			newSuspendedTransaction(Dispatchers.IO, statement = block)
	
	private fun <T> runBlockingTransaction(block: Transaction.() -> T) = transaction(statement = block)
}
