package pl.karol202.sciorder.client.user.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import pl.karol202.sciorder.client.common.components.Event
import pl.karol202.sciorder.client.common.extensions.map
import pl.karol202.sciorder.client.common.extensions.mapNotNull
import pl.karol202.sciorder.client.common.extensions.nonNull
import pl.karol202.sciorder.client.common.extensions.switchMap
import pl.karol202.sciorder.client.common.model.local.owner.OwnerDao
import pl.karol202.sciorder.client.common.model.local.product.ProductDao
import pl.karol202.sciorder.client.common.model.remote.ProductApi
import pl.karol202.sciorder.client.common.repository.product.ProductRepositoryImpl
import pl.karol202.sciorder.client.common.repository.resource.EmptyResource
import pl.karol202.sciorder.client.common.repository.resource.ResourceState
import pl.karol202.sciorder.common.model.Product

class ProductsViewModel(ownerDao: OwnerDao,
                        productDao: ProductDao,
                        productApi: ProductApi) : ViewModel()
{
	private val coroutineJob = Job()
	private val coroutineScope = CoroutineScope(coroutineJob)
	private val productsRepository = ProductRepositoryImpl(coroutineScope, productDao, productApi)

	private val ownerLiveData = ownerDao.get()

	private val productsResourceLiveData = ownerLiveData.nonNull().map { productsRepository.getAllProducts(it.id) }
	private val productsResourceAsLiveData = productsResourceLiveData.switchMap { it.asLiveData }
	private val productsResource get() = productsResourceLiveData.value ?: EmptyResource<List<Product>>()

	val productsLiveData = productsResourceAsLiveData.map { it.data }
	val loadingLiveData = productsResourceAsLiveData.map { it is ResourceState.Loading }
	val errorEventLiveData = productsResourceAsLiveData.mapNotNull { if(it is ResourceState.Failure) Event(Unit) else null }

	fun refreshProducts() = productsResource.reload()
}
