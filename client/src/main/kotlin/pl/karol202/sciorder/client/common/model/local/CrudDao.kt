package pl.karol202.sciorder.client.common.model.local

interface CrudDao<T>
{
	suspend fun insert(items: List<T>)

	suspend fun update(items: List<T>)

	suspend fun delete(items: List<T>)

	suspend fun getAll(): List<T>
}
