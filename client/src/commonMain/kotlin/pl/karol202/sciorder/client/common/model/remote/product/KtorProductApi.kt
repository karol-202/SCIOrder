package pl.karol202.sciorder.client.common.model.remote.product

import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import kotlinx.serialization.json.Json
import kotlinx.serialization.list
import pl.karol202.sciorder.client.common.model.remote.BasicApi
import pl.karol202.sciorder.common.Product

class KtorProductApi(httpClient: HttpClient) : BasicApi(httpClient), ProductApi
{
	override suspend fun addProduct(ownerId: String, hash: String, product: Product) = post<Product> {
		apiUrl("owner/$ownerId/products")
		parameter("hash", hash)
		jsonBody(product)
	}

	override suspend fun updateProduct(ownerId: String, productId: String, hash: String, product: Product) = put<Unit> {
		apiUrl("owner/$ownerId/products/$productId")
		parameter("hash", hash)
		jsonBody(product)
	}

	override suspend fun removeProduct(ownerId: String, productId: String, hash: String) = delete<Unit> {
		apiUrl("owner/$ownerId/products/$productId")
		parameter("hash", hash)
	}

	override suspend fun getAllProducts(ownerId: String) = get<String> {
		apiUrl("owner/$ownerId/products")
	}.map { Json.parse(Product.serializer().list, it) } // Workaround for Ktor's KotlinxSerializer not being able to deserialize lists
}
