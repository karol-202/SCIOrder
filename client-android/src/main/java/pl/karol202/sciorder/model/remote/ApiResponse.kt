package pl.karol202.sciorder.model.remote

import retrofit2.Response

sealed class ApiResponse<T>
{
	companion object
	{
		fun <T> fromRetrofitResponse(response: Response<T>) =
				response.takeIf { it.isSuccessful }?.body()?.let { ApiResponseSuccess(it) } ?: fromErrorResponse(response)

		private fun <T> fromErrorResponse(response: Response<T>) =
				ApiResponseError<T>(response.errorBody()?.string()?.takeIf { it.isNotBlank() } ?: response.message())

		fun <T> fromThrowable(throwable: Throwable) =
				ApiResponseError<T>(throwable.message ?: "")
	}

	class ApiResponseSuccess<T>(val data: T) : ApiResponse<T>()

	class ApiResponseError<T>(val message: String) : ApiResponse<T>()
}