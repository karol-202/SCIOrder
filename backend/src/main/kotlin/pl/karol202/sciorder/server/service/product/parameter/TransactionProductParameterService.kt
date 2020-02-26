package pl.karol202.sciorder.server.service.product.parameter

import pl.karol202.sciorder.common.request.ProductParameterRequest
import pl.karol202.sciorder.server.service.transaction.TransactionService

class TransactionProductParameterService(private val delegate: ProductParameterService,
                                         private val transactionService: TransactionService) : ProductParameterService
{
	override suspend fun insertParameter(storeId: Long,
	                                     productId: Long,
	                                     parameter: ProductParameterRequest) = transactionService.runTransaction {
		delegate.insertParameter(storeId, productId, parameter)
	}
	
	override suspend fun updateParameter(storeId: Long,
	                                     productId: Long,
	                                     parameterId: Long,
	                                     parameter: ProductParameterRequest) = transactionService.runTransaction {
		delegate.updateParameter(storeId, productId, parameterId, parameter)
	}
	
	override suspend fun deleteParameter(storeId: Long, productId: Long, parameterId: Long) = transactionService.runTransaction {
		delegate.deleteParameter(storeId, productId, parameterId)
	}
	
	override suspend fun getParameters(storeId: Long, productId: Long) = transactionService.runTransaction {
		delegate.getParameters(storeId, productId)
	}
}
