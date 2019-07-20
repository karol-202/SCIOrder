package pl.karol202.sciorder.client.js.common.model.local

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.w3c.dom.get
import org.w3c.dom.set
import pl.karol202.sciorder.client.js.common.model.local.NullableLocalDao.Nullable
import kotlin.browser.localStorage

abstract class LocalDao<T : Any>(private val storageId: String,
                                 private val serializer: KSerializer<T>,
                                 initialData: T)
{
	private class NoValueException : Exception()
	
	object ValueBuilder
	{
		fun noValue(): Nothing = throw NoValueException()
	}
	
	companion object
	{
		const val STORAGE_OWNER = "owner"
		const val STORAGE_ORDERS = "orders"
		const val STORAGE_PRODUCTS = "products"
	}
	
	private var updateListeners = listOf<(T) -> Unit>()
	private var loaded = false
	private var data = initialData
	
	protected fun getFromStorage() = channelFlow<T> {
		if(!loaded) loadFromStorage()
		send(data)
		
		val listener: (T) -> Unit = { offer(it) }
		updateListeners += listener
		awaitClose { updateListeners -= listener }
	}
	
	private fun loadFromStorage()
	{
		localStorage[storageId]?.let { data = Json.parse(serializer, it) }
		loaded = true
	}
	
	protected fun setData(builder: ValueBuilder.(oldData: T) -> T)
	{
		fun catchNoValue(block: () -> Unit) = try { block() } catch(e: NoValueException) {}
		
		if(!loaded) loadFromStorage()
		catchNoValue { data = ValueBuilder.builder(data) }
		saveToStorage()
		updateListeners.forEach { it(data) }
	}
	
	private fun saveToStorage()
	{
		localStorage[storageId] = Json.stringify(serializer, data)
	}
}

abstract class NullableLocalDao<T : Any>(storageId: String,
                                         serializer: KSerializer<T>,
                                         initialData: T?) : LocalDao<Nullable<T>>(storageId, 
                                                                                  Nullable.serializer(serializer), 
                                                                                  Nullable(initialData))
{
	@Serializable
	class Nullable<T>(val value: T? = null)
	
	protected fun getNullableFromStorage() = getFromStorage().map { it.value }
	
	protected fun setNullableData(builder: ValueBuilder.(oldData: T?) -> T?) = setData { Nullable(builder(it.value)) }
}
