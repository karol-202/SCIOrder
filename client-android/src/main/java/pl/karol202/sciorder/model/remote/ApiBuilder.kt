package pl.karol202.sciorder.model.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

object ApiBuilder
{
	private const val URL = "http://192.168.0.12:8080/"

	private val okHttp = OkHttpClient.Builder()
		.connectTimeout(3, TimeUnit.SECONDS)
		.readTimeout(3, TimeUnit.SECONDS)
		.build()

	@PublishedApi
	internal val retrofit = Retrofit.Builder()
		.baseUrl(URL)
		.client(okHttp)
		.addConverterFactory(MoshiConverterFactory.create())
		.addCallAdapterFactory(LiveDataCallAdapterFactory())
		.build()

	inline fun <reified T> create() = retrofit.create<T>()
}