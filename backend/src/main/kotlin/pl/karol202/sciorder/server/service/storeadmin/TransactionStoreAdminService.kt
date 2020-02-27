package pl.karol202.sciorder.server.service.storeadmin

import pl.karol202.sciorder.server.service.transaction.TransactionService

class TransactionStoreAdminService(private val delegate: StoreAdminService,
                                   private val transactionService: TransactionService) : StoreAdminService
{
	override suspend fun insertStoreAdminJoin(adminId: Long, storeId: Long) = transactionService.runTransaction {
		delegate.insertStoreAdminJoin(adminId, storeId)
	}
}
