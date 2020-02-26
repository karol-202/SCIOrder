package pl.karol202.sciorder.server.service.product

import pl.karol202.sciorder.common.request.ProductRequest
import pl.karol202.sciorder.server.service.transaction.TransactionService

class TransactionProductService(private val delegate: ProductService,
                                private val transactionService: TransactionService) : ProductService
{
	override suspend fun insertProduct(storeId: Long, product: ProductRequest) = transactionService.runTransaction {
		delegate.insertProduct(storeId, product)
	}
	
	override suspend fun updateProduct(storeId: Long, productId: Long, product: ProductRequest) = transactionService.runTransaction {
		delegate.updateProduct(storeId, productId, product)
	}
	
	override suspend fun deleteProduct(storeId: Long, productId: Long) = transactionService.runTransaction {
		delegate.deleteProduct(storeId, productId)
	}
	
	override suspend fun getProducts(storeId: Long) = transactionService.runTransaction {
		delegate.getProducts(storeId)
	}
	
	override suspend fun getProduct(storeId: Long, productId: Long) = transactionService.runTransaction {
		delegate.getProduct(storeId, productId)
	}
}
