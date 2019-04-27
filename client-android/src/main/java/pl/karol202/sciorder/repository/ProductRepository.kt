package pl.karol202.sciorder.repository

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import pl.karol202.sciorder.model.Product
import pl.karol202.sciorder.model.local.LocalDatabase
import pl.karol202.sciorder.model.local.product.toProductDao
import pl.karol202.sciorder.model.remote.product.ProductApi

interface ProductRepository
{
	companion object
	{
		fun create(coroutineScope: CoroutineScope, context: Context) =
			ProductRepositoryImpl(coroutineScope,
								  LocalDatabase.create(context).productEntityDao().toProductDao(),
								  ProductApi.create())
	}

	fun getAllProducts(): Resource<List<Product>>
}