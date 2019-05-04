package pl.karol202.sciorder.client.user.model.remote

import retrofit2.Response

sealed class ApiResponse<T>
{
	companion object
	{
		fun <T> fromRetrofitResponse(response: Response<T>) =
			response.takeIf { it.isSuccessful }?.body()?.let { Success(it) } ?: fromErrorResponse(response)

		private fun <T> fromErrorResponse(response: Response<T>) =
			Error<T>(response.errorBody()?.string()?.takeIf { it.isNotBlank() } ?: response.message())

		fun <T> fromThrowable(throwable: Throwable) = Error<T>(throwable.message ?: "")
	}

	class Success<T>(val data: T) : ApiResponse<T>()

	class Error<T>(val message: String) : ApiResponse<T>()
}
