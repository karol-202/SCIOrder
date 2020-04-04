package pl.karol202.sciorder.client.common.viewmodel

import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import pl.karol202.sciorder.client.common.api.ApiResponse
import pl.karol202.sciorder.client.common.repository.auth.admin.AdminAuthRepository
import pl.karol202.sciorder.client.common.repository.product.ProductRepository
import pl.karol202.sciorder.client.common.repository.store.StoreRepository
import pl.karol202.sciorder.client.common.util.Event
import pl.karol202.sciorder.client.common.util.IdGenerator
import pl.karol202.sciorder.client.common.util.sendNow
import pl.karol202.sciorder.common.model.Product
import pl.karol202.sciorder.common.model.ProductParameter
import pl.karol202.sciorder.common.request.ProductCreateRequest
import pl.karol202.sciorder.common.request.ProductParameterRequest
import pl.karol202.sciorder.common.request.ProductRequest
import pl.karol202.sciorder.common.request.ProductUpdateRequest
import pl.karol202.sciorder.common.validation.isNameNotBlank
import pl.karol202.sciorder.common.validation.isNameShortEnough

abstract class AdminProductEditViewModel(adminAuthRepository: AdminAuthRepository,
                                         storeRepository: StoreRepository,
                                         private val productRepository: ProductRepository) : ViewModel()
{
	sealed class EditState
	{
		object NoEdit : EditState()
		
		sealed class Edit : EditState(), ProductRequest
		{
			data class New(override val name: String = "",
			               override val available: Boolean = true,
			               val createdParameters: Map<Long, ProductParameterRequest> = emptyMap()) : Edit()
			{
				override val toRequest
					get() = ProductCreateRequest(name, available, createdParameters.values.toList())
				override val parameters
					get() = createdParameters.map { (id, param) -> param.toParameter(id, NEW_PARAMETER_PRODUCT_ID) }
				
				override fun withName(name: String) = copy(name = name)
				
				override fun withAvailability(available: Boolean) = copy(available = available)
				
				override fun withNewParameter(id: Long, parameter: ProductParameterRequest) =
						copy(createdParameters = createdParameters + (id to parameter))
				
				override fun withUpdatedParameter(id: Long, parameter: ProductParameterRequest) = when(id)
				{
					in createdParameters -> copy(createdParameters = createdParameters + (id to parameter))
					else -> this
				}
				
				override fun withRemovedParameter(id: Long) =
						copy(createdParameters = createdParameters - id)
			}
			
			data class Existing(val id: Long,
			                    override val name: String,
                                override val available: Boolean,
                                val createdParameters: Map<Long, ProductParameterRequest> = emptyMap(),
                                val updatedParameters: Map<Long, ProductParameterRequest> = emptyMap(),
                                val removedParameters: List<Long> = emptyList()) : Edit()
			{
				override val toRequest
					get() = ProductUpdateRequest(name, available,
					                             createdParameters.values.toList(), updatedParameters, removedParameters)
				override val parameters
					get() = updatedParameters.map { (id, param) -> param.toParameter(id, this.id) } +
							createdParameters.map { (id, param) -> param.toParameter(id, NEW_PARAMETER_PRODUCT_ID) }
				
				constructor(product: Product) :
						this(product.id, product.name, product.available,
						     updatedParameters = product.parameters.map { it.id to it.toRequest }.toMap())
				
				override fun withName(name: String) = copy(name = name)
				
				override fun withAvailability(available: Boolean) = copy(available = available)
				
				override fun withNewParameter(id: Long, parameter: ProductParameterRequest) =
						copy(createdParameters = createdParameters + (id to parameter))
				
				override fun withUpdatedParameter(id: Long, parameter: ProductParameterRequest) = when(id)
				{
					in createdParameters -> copy(createdParameters = createdParameters + (id to parameter))
					in updatedParameters -> copy(updatedParameters = updatedParameters + (id to parameter))
					else -> this
				}
				
				override fun withRemovedParameter(id: Long) = when(id)
				{
					in createdParameters -> copy(createdParameters = createdParameters - id)
					in updatedParameters -> copy(updatedParameters = updatedParameters - id,
					                             removedParameters = removedParameters + id)
					else -> this
				}
			}
			
			abstract val toRequest: ProductRequest
			abstract val parameters: List<ProductParameter>
			
			abstract fun withName(name: String): Edit
			
			abstract fun withAvailability(available: Boolean): Edit
			
			abstract fun withNewParameter(id: Long, parameter: ProductParameterRequest): Edit
			
			abstract fun withUpdatedParameter(id: Long, parameter: ProductParameterRequest): Edit
			
			abstract fun withRemovedParameter(id: Long): Edit
		}
	}
	
	enum class UpdateError
	{
		NETWORK, PRODUCT_INVALID, OTHER
	}
	
	enum class NameValidationError
	{
		NAME_BLANK, NAME_TOO_LONG
	}
	
	companion object
	{
		const val NEW_PARAMETER_PRODUCT_ID = Long.MIN_VALUE
		const val NEW_PARAMETER_ID_BASE = Long.MIN_VALUE
	}
	
	private val editStateChannel = ConflatedBroadcastChannel<EditState>(EditState.NoEdit)
	private val updateErrorEventChannel = ConflatedBroadcastChannel<Event<UpdateError>>()
	
	private val adminAuthFlow = adminAuthRepository.getAdminAuthFlow()
	private val selectedStoreFlow = storeRepository.getSelectedStoreFlow()
	
	protected val editedProductFlow = editStateChannel
			.asFlow()
			.map { it as? ProductRequest }
			.distinctUntilChanged()
	
	protected val editedParametersFlow = editStateChannel
			.asFlow()
			.map { (it as? EditState.Edit)?.parameters.orEmpty() }
			.distinctUntilChanged()
	
	protected val nameValidationErrorFlow = editStateChannel
			.asFlow()
			.map { validateName(it) }
			.distinctUntilChanged()
	
	protected val updateErrorEventFlow = updateErrorEventChannel
			.asFlow()
			.distinctUntilChanged()
	
	private var idGenerator = IdGenerator(NEW_PARAMETER_ID_BASE)
	
	fun editNewProduct() = editStateChannel.sendNow(EditState.Edit.New())
	
	fun editExistingProduct(product: Product) = editStateChannel.sendNow(EditState.Edit.Existing(product))
	
	fun stopEditing() = editStateChannel.sendNow(EditState.NoEdit)
	
	fun updateProductName(name: String) = updateEdit { withName(name) }
	
	fun updateProductAvailability(available: Boolean) = updateEdit { withAvailability(available) }
	
	fun addParameter() = updateEdit { withNewParameter(idGenerator.nextId(), ProductParameterRequest()) }
	
	fun updateParameter(parameter: ProductParameter) = updateEdit { withUpdatedParameter(parameter.id, parameter.toRequest) }
	
	fun removeParameter(parameterId: Long) = updateEdit { withRemovedParameter(parameterId) }
	
	private fun updateEdit(edit: EditState.Edit.() -> EditState.Edit) =
			(editStateChannel.value as? EditState.Edit)?.edit()?.let { editStateChannel.sendNow(it) }
	
	fun applyProduct() = when(val editState = editStateChannel.value)
	{
		is EditState.Edit.New -> postProduct(editState.toRequest)
		is EditState.Edit.Existing -> putProduct(editState.id, editState.toRequest)
		else -> Unit
	}
	
	fun removeProduct() = when(val editState = editStateChannel.value)
	{
		is EditState.Edit.Existing -> deleteProduct(editState.id)
		else -> Unit
	}
	
	private fun postProduct(product: ProductCreateRequest) = request { token, storeId ->
		productRepository.addProduct(token, storeId, product).handleEditSuccess().handleResponse()
	}
	
	private fun putProduct(productId: Long, product: ProductUpdateRequest) = request { token, storeId ->
		productRepository.updateProduct(token, storeId, productId, product).handleEditSuccess().handleResponse()
	}
	
	private fun deleteProduct(productId: Long) = request { token, storeId ->
		productRepository.removeProduct(token, storeId, productId).handleEditSuccess().handleResponse()
	}
	
	private suspend fun <T> ApiResponse<T>.handleEditSuccess() = ifSuccess { stopEditing() }
	
	private suspend fun <T> ApiResponse<T>.handleResponse() =
			ifFailure { updateErrorEventChannel.sendNow(Event(it.mapError())) }
	
	private fun ApiResponse.Error.mapError() = when(type)
	{
		ApiResponse.Error.Type.NETWORK -> UpdateError.NETWORK
		ApiResponse.Error.Type.BAD_REQUEST -> UpdateError.PRODUCT_INVALID
		else -> UpdateError.OTHER
	}
	
	private fun request(block: suspend (String, Long) -> Unit) = launch {
		val token = adminAuthFlow.first()?.authToken ?: return@launch
		val storeId = selectedStoreFlow.first()?.id ?: return@launch
		block(token, storeId)
	}
	
	private fun validateName(editState: EditState) = when
	{
		editState !is EditState.Edit -> null
		!editState.isNameNotBlank -> NameValidationError.NAME_BLANK
		!editState.isNameShortEnough -> NameValidationError.NAME_TOO_LONG
		else -> null
	}
	
	override fun onCleared()
	{
		super.onCleared()
		updateErrorEventChannel.cancel()
	}
}
