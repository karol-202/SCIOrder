package pl.karol202.sciorder.client.android.user.viewmodel

import pl.karol202.sciorder.client.android.common.components.CoroutineViewModel
import pl.karol202.sciorder.client.android.common.components.Event
import pl.karol202.sciorder.client.android.common.repository.owner.OwnerRepositoryImpl
import pl.karol202.sciorder.client.android.common.repository.product.ProductRepositoryImpl
import pl.karol202.sciorder.client.android.common.repository.resource.EmptyResource
import pl.karol202.sciorder.client.android.common.repository.resource.ResourceState
import pl.karol202.sciorder.client.common.model.remote.OwnerApi
import pl.karol202.sciorder.client.common.model.remote.ProductApi
import pl.karol202.sciorder.common.model.Product

class ProductsViewModel(ownerDao: OwnerDao,
                        ownerApi: OwnerApi,
                        productDao: ProductDao,
                        productApi: ProductApi) : CoroutineViewModel()
{
	private val ownerRepository = OwnerRepositoryImpl(coroutineScope, ownerDao, ownerApi)
	private val productRepository = ProductRepositoryImpl(coroutineScope, productDao, productApi)

	private val ownerLiveData = ownerRepository.getOwner()

	private val productsResourceLiveData = ownerLiveData.nonNull().map { productRepository.getAllProducts(it.id) }
	private val productsResourceAsLiveData = productsResourceLiveData.switchMap { it.asLiveData }
	private val productsResource get() = productsResourceLiveData.value ?: EmptyResource<List<Product>>()

	val productsLiveData = productsResourceAsLiveData.map { it.data }
	val loadingLiveData = productsResourceAsLiveData.map { it is ResourceState.Loading }
	val errorEventLiveData = productsResourceAsLiveData.mapNotNull { if(it is ResourceState.Failure) Event(Unit) else null }

	fun refreshProducts() = productsResource.reload()
}
