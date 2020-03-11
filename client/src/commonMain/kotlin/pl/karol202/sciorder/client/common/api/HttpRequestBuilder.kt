package pl.karol202.sciorder.client.common.api

import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.auth.AuthScheme
import io.ktor.http.contentType

@PublishedApi
internal fun HttpRequestBuilder.relativePath(path: String) = url {
	encodedPath = path
}

@PublishedApi
internal fun HttpRequestBuilder.parameters(key: String, values: List<Any?>) = values.forEach { parameter(key, it) }

@PublishedApi
internal fun HttpRequestBuilder.authToken(value: String) = header(HEADER_AUTHENTICATION, "${AuthScheme.Bearer} $value")

@PublishedApi
internal fun HttpRequestBuilder.jsonBody(body: Any)
{
	contentType(ContentType.Application.Json)
	this.body = body
}

// OkHttp requires body for POST, PUT and other methods
@PublishedApi
internal fun HttpRequestBuilder.emptyBody()
{
	body = ""
}
