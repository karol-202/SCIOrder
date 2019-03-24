package pl.karol202.sciorder.repository

sealed class ResourceState<T>(open val data: T?)
{
	class Success<T>(override val data: T) : ResourceState<T>(data)

	class Loading<T>(data: T?) : ResourceState<T>(data)

	class Failure<T>(data: T?,
	                 val message: String) : ResourceState<T>(data)
}