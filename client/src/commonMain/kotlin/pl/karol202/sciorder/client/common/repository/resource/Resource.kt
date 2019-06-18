package pl.karol202.sciorder.client.common.repository.resource

import kotlinx.coroutines.flow.Flow
import pl.karol202.sciorder.client.common.model.remote.ApiResponse

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
				NOT_FOUND, CONFLICT,
				OTHER;

				companion object
				{
					fun fromApiResponseType(type: ApiResponse.Error.Type) = when(type)
					{
						ApiResponse.Error.Type.NETWORK -> NETWORK
						ApiResponse.Error.Type.NOT_FOUND -> NOT_FOUND
						ApiResponse.Error.Type.CONFLICT -> CONFLICT
						else -> OTHER
					}
				}
			}
		}
	}

	val asFlow: Flow<State<T>>

	suspend fun reload()

	fun close()
}
