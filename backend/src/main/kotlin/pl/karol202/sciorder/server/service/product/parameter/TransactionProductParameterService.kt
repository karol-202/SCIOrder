package pl.karol202.sciorder.server.service.product.parameter

import pl.karol202.sciorder.common.request.ProductParameterRequest
import pl.karol202.sciorder.server.service.transaction.TransactionService
import pl.karol202.sciorder.server.service.transaction.invoke

class TransactionProductParameterService(private val delegate: ProductParameterService,
                                         private val transactionService: TransactionService) : ProductParameterService
{
	override suspend fun insertParameter(storeId: Long,
	                                     productId: Long,
	                                     parameter: ProductParameterRequest) = transactionService {
		delegate.insertParameter(storeId, productId, parameter)
	}
	
	override suspend fun updateParameter(storeId: Long,
	                                     productId: Long,
	                                     parameterId: Long,
	                                     parameter: ProductParameterRequest) = transactionService {
		delegate.updateParameter(storeId, productId, parameterId, parameter)
	}
	
	override suspend fun deleteParameter(storeId: Long, productId: Long, parameterId: Long) = transactionService {
		delegate.deleteParameter(storeId, productId, parameterId)
	}
	
	override suspend fun getParameters(storeId: Long, productId: Long) = transactionService {
		delegate.getParameters(storeId, productId)
	}
}
