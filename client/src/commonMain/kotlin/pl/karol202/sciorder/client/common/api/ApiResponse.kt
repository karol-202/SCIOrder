package pl.karol202.sciorder.client.common.api

import io.ktor.client.features.ResponseException
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.TimeoutCancellationException

sealed class ApiResponse<out T>
{
	companion object
	{
		fun <T> fromData(data: T) = Success(data)
		
		fun fromThrowable(throwable: Throwable) = Error(throwable.getErrorType(), throwable.message ?: "")

		private fun Throwable.getErrorType() = when(this)
		{
			is IOException -> Error.Type.NETWORK
			is ResponseException -> when(response.status)
			{
				HttpStatusCode.BadRequest -> Error.Type.BAD_REQUEST
				HttpStatusCode.Unauthorized -> Error.Type.UNAUTHORIZED
				HttpStatusCode.Forbidden -> Error.Type.FORBIDDEN
				HttpStatusCode.NotFound -> Error.Type.NOT_FOUND
				HttpStatusCode.Conflict -> Error.Type.CONFLICT
				else -> Error.Type.OTHER
			}
			is TimeoutCancellationException -> Error.Type.NETWORK
			else -> Error.Type.OTHER
		}
	}

	data class Success<T>(val data: T) : ApiResponse<T>()
	
	data class Error(val type: Type,
	                 val message: String = "") : ApiResponse<Nothing>()
	{
		enum class Type
		{
			NETWORK,
			BAD_REQUEST, UNAUTHORIZED, FORBIDDEN, NOT_FOUND, CONFLICT,
			LOCAL_INCONSISTENCY, // Indicates that local client's state makes request impossible or pointless,
								 // may be caused for instance by trying to update order that has been just deleted.
			OTHER
		}
	}

	suspend fun <R> map(mapper: suspend (T) -> R): ApiResponse<R> = when(this)
	{
		is Success -> Success(mapper(data))
		is Error -> this
	}
	
	suspend fun <R> flatMap(mapper: suspend (T) -> ApiResponse<R>): ApiResponse<R> = when(this)
	{
		is Success -> mapper(data)
		is Error -> this
	}

	suspend fun fold(onSuccess: suspend (T) -> Unit = { },
	                 onError: suspend (Error) -> Unit = { }): ApiResponse<T> = when(this)
	{
		is Success -> apply { onSuccess(data) }
		is Error -> apply { onError(this as Error /*Fixed in new type inference*/) }
	}

	suspend fun ifSuccess(onSuccess: suspend (T) -> Unit) = fold(onSuccess = onSuccess)

	suspend fun ifFailure(onFailure: suspend (Error) -> Unit) = fold(onError = onFailure)
}

suspend fun <T1, T2, R> merge(first: ApiResponse<T1>, second: ApiResponse<T2>, mapper: suspend (T1, T2) -> R) =
		first.flatMap { t -> second.map { t2 -> mapper(t, t2) } }
