package pl.karol202.sciorder.client.common.model.remote

sealed class ApiResponse<out T>
{
	companion object
	{
		fun <T> fromData(data: T) = Success(data)

		fun fromThrowable(throwable: Throwable) =
				Error(throwable.getErrorType(), throwable.message ?: "")

		private fun Throwable.getErrorType() = when(this)
		{
			// TODO Find possible exceptions
			//is IOException -> ApiResponse.Error.Type.NETWORK
			else ->
			{
				throw this
				Error.Type.OTHER
			}
		}
	}

	class Success<T>(val data: T) : ApiResponse<T>()

	// Message is for debugging purposes
	class Error(val type: Type,
	            val message: String) : ApiResponse<Nothing>()
	{
		enum class Type
		{
			NETWORK,
			NOT_FOUND, CONFLICT,
			OTHER
		}
	}

	suspend fun fold(onSuccess: suspend (T) -> Unit = { },
	                 onError: suspend (Error) -> Unit = { }): ApiResponse<T> = apply {
		when(this)
		{
			is Success -> onSuccess(data)
			is Error -> onError(this)
		}
	}

	suspend fun ifSuccess(onSuccess: suspend (T) -> Unit) = fold(onSuccess = onSuccess)

	suspend fun ifFailure(onFailure: suspend (Error) -> Unit) = fold(onError = onFailure)
}
