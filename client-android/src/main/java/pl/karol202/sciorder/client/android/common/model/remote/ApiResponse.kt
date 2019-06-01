package pl.karol202.sciorder.client.android.common.model.remote

import retrofit2.Response

sealed class ApiResponse<T>
{
	companion object
	{
		fun <T> fromRetrofitResponse(response: Response<T>) =
			response.takeIf { it.isSuccessful }?.body()?.let { Success(it) } ?: fromErrorResponse(response)

		private fun <T> fromErrorResponse(response: Response<T>) =
			Error<T>(response.errorBody()?.string()?.takeIf { it.isNotBlank() } ?: response.message(), response.code())

		fun <T> fromThrowable(throwable: Throwable) = Error<T>(throwable.message ?: "", null)
	}

	class Success<T>(val data: T) : ApiResponse<T>()

	class Error<T>(val message: String,
	               val code: Int?) : ApiResponse<T>()
}
