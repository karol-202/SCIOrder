package pl.karol202.sciorder.client.android.common.model.remote

import okhttp3.Request
import pl.karol202.sciorder.client.common.model.remote.ApiResponse
import retrofit2.*
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ApiResponseCallAdapter<T>(private val responseType: Type) : CallAdapter<T, ApiResponseCallAdapter.ApiResponseCall<T>>
{
	class Factory : CallAdapter.Factory()
	{
		override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *>?
		{
			if(getRawType(returnType) != Call::class.java) return null
			val callType = getParameterUpperBound(0, returnType as ParameterizedType)

			if(getRawType(callType) != ApiResponse::class.java) return null
			val apiResponseType = getParameterUpperBound(0, callType as ParameterizedType)

			return ApiResponseCallAdapter<Any>(apiResponseType)
		}
	}

	class ApiResponseCall<T>(private val originalCall: Call<T>) : Call<ApiResponse<T>>
	{
		override fun execute(): Response<ApiResponse<T>> = Response.success(executeForApiResponse())

		private fun executeForApiResponse() =
				try { ApiResponse.fromResponse(originalCall.execute()) }
				catch(t: Throwable) { ApiResponse.fromThrowable(t) }

		override fun enqueue(callback: Callback<ApiResponse<T>>)
		{
			originalCall.enqueue(object : Callback<T> {
				override fun onResponse(call: Call<T>, response: Response<T>) =
						callback.onResponse(this@ApiResponseCall, Response.success(ApiResponse.fromResponse(response)))

				override fun onFailure(call: Call<T>, t: Throwable) =
						callback.onResponse(this@ApiResponseCall, Response.success(ApiResponse.fromThrowable(t)))
			})
		}

		override fun isExecuted() = originalCall.isExecuted

		override fun isCanceled() = originalCall.isCanceled

		override fun cancel() = originalCall.cancel()

		override fun clone() = ApiResponseCall(originalCall.clone())

		override fun request(): Request = originalCall.request()
	}

	override fun responseType() = responseType

	override fun adapt(call: Call<T>) = ApiResponseCall(call)
}
