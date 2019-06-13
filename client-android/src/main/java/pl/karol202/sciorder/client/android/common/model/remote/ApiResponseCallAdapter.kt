package pl.karol202.sciorder.client.android.common.model.remote

import okhttp3.Request
import pl.karol202.sciorder.client.android.common.extensions.fromResponse
import pl.karol202.sciorder.client.android.common.extensions.fromThrowable
import pl.karol202.sciorder.client.common.model.remote.ApiResponse
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ApiResponseCallAdapter<T>(private val responseType: Type) : CallAdapter<T, ApiResponseCallAdapter.Call<T>>
{
	class Factory : CallAdapter.Factory()
	{
		override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *>?
		{
			if(getRawType(returnType) != ApiResponse::class.java) return null

			val apiResponseType = getParameterUpperBound(0, returnType as ParameterizedType)
			return ApiResponseCallAdapter<Any>(apiResponseType)
		}
	}

	class Call<T>(private val originalCall: retrofit2.Call<T>) : retrofit2.Call<ApiResponse<T>>
	{
		override fun execute(): Response<ApiResponse<T>> = Response.success(executeForApiResponse())

		private fun executeForApiResponse() =
				try { ApiResponse.fromResponse(originalCall.execute()) }
				catch(t: Throwable) { ApiResponse.fromThrowable(t) }

		override fun enqueue(callback: Callback<ApiResponse<T>>)
		{
			originalCall.enqueue(object : Callback<T> {
				override fun onResponse(call: retrofit2.Call<T>, response: Response<T>) =
						callback.onResponse(this@Call, Response.success(ApiResponse.fromResponse(response)))

				override fun onFailure(call: retrofit2.Call<T>, t: Throwable) =
						callback.onResponse(this@Call, Response.success(ApiResponse.fromThrowable(t)))
			})
		}

		override fun isExecuted() = originalCall.isExecuted

		override fun isCanceled() = originalCall.isCanceled

		override fun cancel() = originalCall.cancel()

		override fun clone() = Call(originalCall.clone())

		override fun request(): Request = originalCall.request()
	}

	override fun responseType() = responseType

	override fun adapt(call: retrofit2.Call<T>) = Call(call)
}
