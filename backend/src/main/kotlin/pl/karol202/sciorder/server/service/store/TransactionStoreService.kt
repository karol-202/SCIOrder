package pl.karol202.sciorder.server.service.store

import pl.karol202.sciorder.common.request.StoreRequest
import pl.karol202.sciorder.server.service.transaction.TransactionService
import pl.karol202.sciorder.server.service.transaction.invoke

class TransactionStoreService(private val delegate: StoreService,
                              private val transactionService: TransactionService) : StoreService
{
	override suspend fun insertStore(store: StoreRequest) = transactionService {
		delegate.insertStore(store)
	}
	
	override suspend fun deleteStore(storeId: Long) = transactionService {
		delegate.deleteStore(storeId)
	}
	
	override suspend fun getStoresByAdmin(adminId: Long) = transactionService {
		delegate.getStoresByAdmin(adminId)
	}
}
