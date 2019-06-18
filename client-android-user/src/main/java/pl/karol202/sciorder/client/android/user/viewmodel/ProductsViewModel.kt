package pl.karol202.sciorder.client.android.user.viewmodel

import kotlinx.coroutines.flow.*
import pl.karol202.sciorder.client.common.components.Event
import pl.karol202.sciorder.client.common.extensions.shareIn
import pl.karol202.sciorder.client.common.repository.owner.OwnerRepository
import pl.karol202.sciorder.client.common.repository.product.ProductRepository
import pl.karol202.sciorder.client.common.repository.resource.Resource
import pl.karol202.sciorder.client.common.viewmodel.CoroutineViewModel

class ProductsViewModel(ownerRepository: OwnerRepository,
                        productRepository: ProductRepository) : CoroutineViewModel()
{
	private val ownerFlow = ownerRepository.getOwnerFlow().conflate().shareIn(coroutineScope)

	private val productsResourceFlow = ownerFlow.filterNotNull().map { productRepository.getProductsResource(it.id) }
												.conflate().shareIn(coroutineScope)
	private val productsResourceAsFlow = productsResourceFlow.switchMap { it.asFlow }

	val productsLiveData = productsResourceAsFlow.map { it.data }.asLiveData()
	val loadingLiveData = productsResourceAsFlow.map { it is Resource.State.Loading }.asLiveData()
	val errorEventLiveData = productsResourceAsFlow.mapNotNull { if(it is Resource.State.Failure) Event(Unit) else null }
												   .asLiveData()

	fun refreshProducts() = launch { productsResourceFlow.first().reload() }
}
