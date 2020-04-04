package pl.karol202.sciorder.client.android.common.viewmodel

import pl.karol202.sciorder.client.android.common.util.asLiveData
import pl.karol202.sciorder.client.common.repository.auth.admin.AdminAuthRepository
import pl.karol202.sciorder.client.common.repository.product.ProductRepository
import pl.karol202.sciorder.client.common.repository.store.StoreRepository
import pl.karol202.sciorder.client.common.viewmodel.AdminProductEditViewModel

class AdminProductEditAndroidViewModel(adminAuthRepository: AdminAuthRepository,
                                       storeRepository: StoreRepository,
                                       productRepository: ProductRepository) :
		AdminProductEditViewModel(adminAuthRepository, storeRepository, productRepository)
{
	val editedProductLiveData = editedProductFlow.asLiveData(coroutineScope)
	val editedParametersLiveData = editedParametersFlow.asLiveData(coroutineScope)
	val validationErrorLiveData = nameValidationErrorFlow.asLiveData(coroutineScope)
	val updateErrorEventLiveData = updateErrorEventFlow.asLiveData(coroutineScope)
}
