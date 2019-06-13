package pl.karol202.sciorder.client.android.common.extensions

import pl.karol202.sciorder.client.common.model.remote.ApiResponse
import retrofit2.Response
import java.io.IOException

fun <T> ApiResponse.Companion.fromResponse(response: Response<T>) =
		response.takeIf { it.isSuccessful }?.let { fromSuccessfulResponse(it) } ?: fromErrorResponse(response)

private fun <T> fromSuccessfulResponse(response: Response<T>) =
		response.body()?.let { ApiResponse.Success(it) }

private fun <T> fromErrorResponse(response: Response<T>) =
		ApiResponse.Error(response.getErrorType(), response.message())

fun ApiResponse.Companion.fromThrowable(throwable: Throwable) =
		ApiResponse.Error(throwable.getErrorType(), throwable.message ?: "")

private fun <T> Response<T>.getErrorType() = when(code())
{
	404 -> ApiResponse.Error.Type.NOT_FOUND
	409 -> ApiResponse.Error.Type.CONFLICT
	else -> ApiResponse.Error.Type.OTHER
}

private fun Throwable.getErrorType() = when(this)
{
	is IOException -> ApiResponse.Error.Type.NETWORK_ERROR
	else -> ApiResponse.Error.Type.OTHER
}
