package pl.karol202.sciorder.client.common.model.remote

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer

fun createApiHttpClient(engineFactory: HttpClientEngineFactory<*>? = null) =
		if(engineFactory != null) HttpClient(engineFactory, HttpClientConfig<*>::configApiHttpClient)
		else HttpClient(HttpClientConfig<*>::configApiHttpClient)

private fun HttpClientConfig<*>.configApiHttpClient() {
	install(JsonFeature) {
		serializer = KotlinxSerializer()
	}
}
