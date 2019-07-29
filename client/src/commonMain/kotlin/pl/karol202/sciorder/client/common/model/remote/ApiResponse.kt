package pl.karol202.sciorder.client.common.model.remote

import io.ktor.client.features.ResponseException
import io.ktor.http.HttpStatusCode
import kotlinx.io.IOException

sealed class ApiResponse<out T>
{
	companion object
	{
		fun <T> fromData(data: T) = Success(data)
		
		fun fromThrowable(throwable: Throwable) =
				Error(throwable.getErrorType(), throwable.message ?: "")

		private fun Throwable.getErrorType() = when(this)
		{
			is IOException -> Error.Type.NETWORK
			is ResponseException -> when(response.status)
			{
				HttpStatusCode.NotFound -> Error.Type.NOT_FOUND
				HttpStatusCode.Conflict -> Error.Type.CONFLICT
				else -> Error.Type.OTHER
			}
			else -> Error.Type.OTHER
		}
	}

	class Success<T>(val data: T) : ApiResponse<T>()
	{
		override fun <X> map(mapper: (T) -> X) = Success(mapper(data))
	}

	// Message is for debugging purposes
	class Error(val type: Type,
	            val message: String = "") : ApiResponse<Nothing>()
	{
		enum class Type
		{
			NETWORK,
			NOT_FOUND, CONFLICT,
			LOCAL_INCONSISTENCY, // Indicates that local client's state makes request impossible or pointless,
								 // may be caused for instance by trying to update order that has been just deleted.
			OTHER
		}

		override fun <X> map(mapper: (Nothing) -> X) = this
	}

	abstract fun <X> map(mapper: (T) -> X): ApiResponse<X>

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
