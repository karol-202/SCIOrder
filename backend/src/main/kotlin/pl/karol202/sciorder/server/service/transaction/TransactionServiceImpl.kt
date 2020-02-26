package pl.karol202.sciorder.server.service.transaction

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class TransactionServiceImpl : TransactionService
{
	override suspend fun <T> runTransaction(transaction: suspend Transaction.() -> T) =
			newSuspendedTransaction(Dispatchers.IO, statement = transaction)
}
