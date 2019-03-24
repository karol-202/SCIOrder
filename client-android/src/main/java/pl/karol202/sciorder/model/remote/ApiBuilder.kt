package pl.karol202.sciorder.model.remote

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

object ApiBuilder
{
	private const val URL = "http://192.168.0.11:8080/"

	@PublishedApi
	internal val retrofit = Retrofit.Builder()
		.baseUrl(URL)
		.addConverterFactory(MoshiConverterFactory.create())
		.addCallAdapterFactory(LiveDataCallAdapterFactory())
		.build()

	inline fun <reified T> create() = retrofit.create<T>()
}