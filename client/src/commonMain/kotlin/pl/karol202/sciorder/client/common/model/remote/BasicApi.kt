package pl.karol202.sciorder.client.common.model.remote

import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.parameter
import io.ktor.client.request.request
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.takeFrom

abstract class BasicApi(protected val httpClient: HttpClient,
                        private val serverUrl: String)
{
	protected suspend inline fun <reified T> apiRequest(builder: HttpRequestBuilder.() -> Unit) = executeForApiResponse {
		httpClient.request<T>(builder)
	}

	protected inline fun <T> executeForApiResponse(block: () -> T) =
			try { ApiResponse.fromData(block()) }
			catch(t: Throwable) { ApiResponse.fromThrowable(t) }

	protected fun HttpRequestBuilder.apiUrl(path: String) = url {
		takeFrom(serverUrl)
		encodedPath = path
	}

	protected fun HttpRequestBuilder.parameters(key: String, values: List<Any?>) = values.forEach { parameter(key, it) }

	protected fun HttpRequestBuilder.json() = contentType(ContentType.Application.Json)
}
