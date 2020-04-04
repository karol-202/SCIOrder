package pl.karol202.sciorder.server.service.product

import pl.karol202.sciorder.common.request.ProductCreateRequest
import pl.karol202.sciorder.common.request.ProductUpdateRequest
import pl.karol202.sciorder.server.service.transaction.TransactionService
import pl.karol202.sciorder.server.service.transaction.invoke

class TransactionProductService(private val delegate: ProductService,
                                private val transactionService: TransactionService) : ProductService
{
	override suspend fun insertProduct(storeId: Long, product: ProductCreateRequest) = transactionService {
		delegate.insertProduct(storeId, product)
	}
	
	override suspend fun updateProduct(storeId: Long, productId: Long, product: ProductUpdateRequest) = transactionService {
		delegate.updateProduct(storeId, productId, product)
	}
	
	override suspend fun deleteProduct(storeId: Long, productId: Long) = transactionService {
		delegate.deleteProduct(storeId, productId)
	}
	
	override suspend fun getProducts(storeId: Long) = transactionService {
		delegate.getProducts(storeId)
	}
	
	override suspend fun getProduct(storeId: Long, productId: Long) = transactionService {
		delegate.getProduct(storeId, productId)
	}
}
