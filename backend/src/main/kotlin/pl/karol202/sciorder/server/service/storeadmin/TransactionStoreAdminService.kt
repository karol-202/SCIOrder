package pl.karol202.sciorder.server.service.storeadmin

import pl.karol202.sciorder.server.service.transaction.TransactionService
import pl.karol202.sciorder.server.service.transaction.invoke

class TransactionStoreAdminService(private val delegate: StoreAdminService,
                                   private val transactionService: TransactionService) : StoreAdminService
{
	override suspend fun insertStoreAdminJoin(adminId: Long, storeId: Long) = transactionService {
		delegate.insertStoreAdminJoin(adminId, storeId)
	}
}
