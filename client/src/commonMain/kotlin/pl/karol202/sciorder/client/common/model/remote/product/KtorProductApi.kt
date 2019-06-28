package pl.karol202.sciorder.client.common.model.remote.product

import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.http.HttpMethod
import pl.karol202.sciorder.client.common.model.remote.BasicApi
import pl.karol202.sciorder.common.Product

class KtorProductApi(httpClient: HttpClient,
                     serverUrl: String) : BasicApi(httpClient, serverUrl), ProductApi
{
	override suspend fun addProduct(ownerId: String, hash: String, product: Product) = apiRequest<Product> {
		method = HttpMethod.Post
		apiUrl("owner/$ownerId/products")
		parameter("hash", hash)
		json()
		body = product
	}

	override suspend fun updateProduct(ownerId: String, productId: String, hash: String, product: Product) = apiRequest<Unit> {
		method = HttpMethod.Put
		apiUrl("owner/$ownerId/products/$productId")
		parameter("hash", hash)
		json()
		body = product
	}

	override suspend fun removeProduct(ownerId: String, productId: String, hash: String) = apiRequest<Unit> {
		method = HttpMethod.Delete
		apiUrl("owner/$ownerId/products/$productId")
		parameter("hash", hash)
	}

	override suspend fun getAllProducts(ownerId: String) = apiRequest<List<Product>> {
		method = HttpMethod.Get
		apiUrl("owner/$ownerId/products")
	}
}