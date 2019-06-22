package pl.karol202.sciorder.client.android.user.viewmodel

import pl.karol202.sciorder.client.android.common.viewmodel.ProductsViewModel
import pl.karol202.sciorder.client.common.repository.owner.OwnerRepository
import pl.karol202.sciorder.client.common.repository.product.ProductRepository

class ProductsViewModel(ownerRepository: OwnerRepository,
                        productRepository: ProductRepository) : ProductsViewModel(ownerRepository, productRepository)
