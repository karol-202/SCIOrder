package pl.karol202.sciorder.client.common.database.dao

interface InsertDao<T>
{
	suspend fun insert(items: List<T>)
}

interface UpdateDao<T>
{
	suspend fun update(items: List<T>)
}

interface DeleteDao<T>
{
	suspend fun delete(items: List<T>)
}

suspend fun <T> InsertDao<T>.insert(item: T) = insert(listOf(item))

suspend fun <T> UpdateDao<T>.update(item: T) = update(listOf(item))

suspend fun <T> DeleteDao<T>.delete(item: T) = delete(listOf(item))
