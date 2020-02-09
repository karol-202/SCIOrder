package pl.karol202.sciorder.client.common.api

import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.http.HttpMethod
import kotlinx.coroutines.withTimeout

class KtorBasicApi(@PublishedApi internal val httpClient: HttpClient,
                   @PublishedApi internal val baseUrl: String,
                   @PublishedApi internal val timeoutMillis: Long = 5000L)
{
	internal suspend inline fun <reified T> get(crossinline builder: HttpRequestBuilder.() -> Unit) = apiRequest<T> {
		method = HttpMethod.Get
		builder()
	}
	
	internal suspend inline fun <reified T> post(crossinline builder: HttpRequestBuilder.() -> Unit) = apiRequest<T> {
		method = HttpMethod.Post
		emptyBody()
		builder()
	}
	
	internal suspend inline fun <reified T> put(crossinline builder: HttpRequestBuilder.() -> Unit) = apiRequest<T> {
		method = HttpMethod.Put
		emptyBody()
		builder()
	}
	
	internal suspend inline fun <reified T> delete(crossinline builder: HttpRequestBuilder.() -> Unit) = apiRequest<T> {
		method = HttpMethod.Delete
		builder()
	}

	@PublishedApi
	internal suspend inline fun <reified T> apiRequest(crossinline builder: HttpRequestBuilder.() -> Unit) = executeForApiResponse {
		withTimeout(timeoutMillis) {
			httpClient.request<T> {
				url(baseUrl)
				builder()
			}
		}
	}

	@PublishedApi
	internal inline fun <T> executeForApiResponse(block: () -> T) =
			runCatching(block).map { ApiResponse.fromData(it) }.getOrElse { ApiResponse.fromThrowable(it) }
}
