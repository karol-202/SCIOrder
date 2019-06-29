package pl.karol202.sciorder.client.common.model.remote

import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.parameter
import io.ktor.client.request.request
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import io.ktor.http.takeFrom

abstract class BasicApi(protected val httpClient: HttpClient,
                        private val serverUrl: String)
{
	protected suspend inline fun <reified T> get(builder: HttpRequestBuilder.() -> Unit) = apiRequest<T> {
		method = HttpMethod.Get
		builder()
	}

	protected suspend inline fun <reified T> post(builder: HttpRequestBuilder.() -> Unit) = apiRequest<T> {
		method = HttpMethod.Post
		emptyBody()
		builder()
	}

	protected suspend inline fun <reified T> put(builder: HttpRequestBuilder.() -> Unit) = apiRequest<T> {
		method = HttpMethod.Put
		emptyBody()
		builder()
	}

	protected suspend inline fun <reified T> delete(builder: HttpRequestBuilder.() -> Unit) = apiRequest<T> {
		method = HttpMethod.Delete
		builder()
	}

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

	protected fun HttpRequestBuilder.jsonBody(body: Any)
	{
		contentType(ContentType.Application.Json)
		this.body = body
	}

	// OkHttp requires body for POST, PUT and other methods
	protected fun HttpRequestBuilder.emptyBody()
	{
		body = ""
	}
}
