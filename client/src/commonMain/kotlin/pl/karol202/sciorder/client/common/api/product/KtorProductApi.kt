package pl.karol202.sciorder.client.common.api.product

import io.ktor.client.request.parameter
import pl.karol202.sciorder.client.common.api.KtorBasicApi
import pl.karol202.sciorder.client.common.api.jsonBody
import pl.karol202.sciorder.client.common.api.relativePath
import pl.karol202.sciorder.common.model.Product

class KtorProductApi(private val basicApi: KtorBasicApi) : ProductApi
{
	override suspend fun addProduct(ownerId: String, hash: String, product: Product) = basicApi.post<Product> {
		relativePath("owner/$ownerId/products")
		parameter("hash", hash)
		jsonBody(product)
	}

	override suspend fun updateProduct(ownerId: String, productId: String, hash: String, product: Product) = basicApi.put<Unit> {
		relativePath("owner/$ownerId/products/$productId")
		parameter("hash", hash)
		jsonBody(product)
	}

	override suspend fun removeProduct(ownerId: String, productId: String, hash: String) = basicApi.delete<Unit> {
		relativePath("owner/$ownerId/products/$productId")
		parameter("hash", hash)
	}

	override suspend fun getAllProducts(ownerId: String) = basicApi.get<List<Product>> {
		relativePath("owner/$ownerId/products")
	}
}
