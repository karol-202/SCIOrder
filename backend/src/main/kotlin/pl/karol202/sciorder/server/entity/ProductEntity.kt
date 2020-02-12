package pl.karol202.sciorder.server.entity

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import pl.karol202.sciorder.common.model.Product
import pl.karol202.sciorder.server.table.ProductParameters
import pl.karol202.sciorder.server.table.Products
import pl.karol202.sciorder.server.util.MappableEntity
import pl.karol202.sciorder.server.util.toModels

class ProductEntity(id: EntityID<Long>) : LongEntity(id), MappableEntity<Product>
{
	companion object : LongEntityClass<ProductEntity>(Products)
	
	var store by StoreEntity referencedOn Products.storeId
	var name by Products.name
	var available by Products.available
	
	val parameters by ProductParameterEntity referrersOn ProductParameters.productId
	
	override fun toModel() = Product(id = id.value,
	                                 storeId = store.id.value,
	                                 name = name,
	                                 available = available,
	                                 parameters = parameters.toModels())
}
