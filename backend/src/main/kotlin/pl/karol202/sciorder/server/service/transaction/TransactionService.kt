package pl.karol202.sciorder.server.service.transaction

import org.jetbrains.exposed.sql.Transaction

interface TransactionService
{
	suspend fun <T> runTransaction(block: suspend Transaction.() -> T): T
}
