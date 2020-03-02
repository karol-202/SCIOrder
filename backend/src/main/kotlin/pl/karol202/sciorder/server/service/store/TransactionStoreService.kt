package pl.karol202.sciorder.server.service.store

import pl.karol202.sciorder.common.request.StoreRequest
import pl.karol202.sciorder.server.service.transaction.TransactionService

class TransactionStoreService(private val delegate: StoreService,
                              private val transactionService: TransactionService) : StoreService
{
	override suspend fun insertStore(store: StoreRequest) = transactionService.runTransaction {
		delegate.insertStore(store)
	}
	
	override suspend fun deleteStore(storeId: Long) = transactionService.runTransaction {
		delegate.deleteStore(storeId)
	}
	
	override suspend fun getStoresByAdmin(adminId: Long) = transactionService.runTransaction {
		delegate.getStoresByAdmin(adminId)
	}
}
