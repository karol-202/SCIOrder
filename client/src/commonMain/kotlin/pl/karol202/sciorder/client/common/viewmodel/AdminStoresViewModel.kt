package pl.karol202.sciorder.client.common.viewmodel

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import pl.karol202.sciorder.client.common.api.ApiResponse
import pl.karol202.sciorder.client.common.model.storeInfo
import pl.karol202.sciorder.client.common.repository.auth.admin.AdminAuthRepository
import pl.karol202.sciorder.client.common.repository.resource.Resource
import pl.karol202.sciorder.client.common.repository.store.StoreRepository
import pl.karol202.sciorder.client.common.util.Event
import pl.karol202.sciorder.client.common.util.conflatedBroadcastIn
import pl.karol202.sciorder.common.model.Store
import pl.karol202.sciorder.common.request.StoreRequest

abstract class AdminStoresViewModel(adminAuthRepository: AdminAuthRepository,
                                    private val storeRepository: StoreRepository) : ViewModel()
{
	enum class UpdateResult
	{
		SUCCESS, FAILURE
	}
	
	private val updateEventChannel = ConflatedBroadcastChannel<Event<UpdateResult>>()
	
	private val adminAuthFlow = adminAuthRepository.getAdminAuthFlow()
	
	private val storesResourceChannel = adminAuthFlow
			.map { it?.let { storeRepository.getStoresResource(it.authToken) } }
			.scan(null as Resource<List<Store>>?) { previous, current -> previous?.close(); current }
			.onEach { it?.autoReloadIn(coroutineScope) }
			.conflatedBroadcastIn(coroutineScope, start = CoroutineStart.DEFAULT)
	
	private val storesStateFlow = storesResourceChannel
			.asFlow()
			.flatMapLatest { it?.asFlow ?: flowOf(null) }
	
	protected val storesFlow = storesStateFlow
			.map { it?.data.orEmpty() }
			.map { stores -> stores.map { it.storeInfo } }
			.distinctUntilChanged()
	
	protected val loadingFlow = storesStateFlow
			.map { it is Resource.State.Loading }
			.distinctUntilChanged()
	
	protected val loadingErrorEventFlow = storesStateFlow
			.mapNotNull { if(it is Resource.State.Failure) Event(Unit) else null }
			.distinctUntilChanged()
	
	protected val updateEventFlow = updateEventChannel
			.asFlow()
			.distinctUntilChanged()
	
	protected val selectedStoreFlow = storeRepository.getSelectedStoreFlow()
	
	fun refreshStores() = launch { storesResourceChannel.valueOrNull?.reload() }
	
	fun addStore(store: StoreRequest) = launch {
		val token = adminAuthFlow.first()?.authToken ?: return@launch
		storeRepository.addStore(token, store).handleResponse()
	}
	
	fun removeStore(storeId: Long) = launch {
		val token = adminAuthFlow.first()?.authToken ?: return@launch
		storeRepository.removeStore(token, storeId).handleResponse()
	}
	
	private suspend fun <T> ApiResponse<T>.handleResponse() =
			fold(onSuccess = { updateEventChannel.offer(Event(UpdateResult.SUCCESS)) },
			     onError = { updateEventChannel.offer(Event(UpdateResult.FAILURE)) })
	
	fun selectStore(storeId: Long?) = launch {
		storeRepository.selectStore(storeId)
	}
	
	override fun onCleared()
	{
		super.onCleared()
		storesResourceChannel.valueOrNull?.close()
		storesResourceChannel.cancel()
	}
}
