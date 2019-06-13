package pl.karol202.sciorder.client.common.model.remote

sealed class ApiResponse<out T>
{
	companion object

	class Success<T>(val data: T) : ApiResponse<T>()

	class Error(val type: Type,
	            val message: String) : ApiResponse<Nothing>()
	{
		enum class Type
		{
			NETWORK_ERROR,
			NOT_FOUND, CONFLICT,
			OTHER
		}
	}
}
