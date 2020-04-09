package pl.karol202.sciorder.client.common.repository.resource

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import pl.karol202.sciorder.client.common.api.ApiResponse

interface Resource<T>
{
	sealed class State<T>(open val data: T?)
	{
		data class Success<T>(override val data: T) : State<T>(data)

		data class Loading<T>(override val data: T?) : State<T>(data)

		data class Failure<T>(override val data: T?,
		                      val type: Type) : State<T>(data)
		{
			enum class Type
			{
				NETWORK,
				NOT_FOUND,
				OTHER;

				companion object
				{
					fun fromApiResponseType(type: ApiResponse.Error.Type) = when(type)
					{
						ApiResponse.Error.Type.NETWORK -> NETWORK
						ApiResponse.Error.Type.NOT_FOUND -> NOT_FOUND
						else -> OTHER
					}
				}
			}
		}
		
		fun <R> map(transform: (T) -> R): State<R> = when(this)
		{
			is Success -> Success(transform(data))
			is Loading -> Loading(data?.let(transform))
			is Failure -> Failure(data?.let(transform), type)
		}
	}

	val asFlow: Flow<State<T>>

	suspend fun autoReloadIn(coroutineScope: CoroutineScope)

	suspend fun reload()

	suspend fun close()
}
