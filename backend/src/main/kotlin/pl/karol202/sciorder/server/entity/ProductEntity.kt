package pl.karol202.sciorder.server.entity

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import pl.karol202.sciorder.common.model.Product
import pl.karol202.sciorder.server.entity.mapping.Mappable
import pl.karol202.sciorder.server.entity.mapping.Updatable
import pl.karol202.sciorder.server.entity.mapping.dispatchTo
import pl.karol202.sciorder.server.entity.mapping.map
import pl.karol202.sciorder.server.table.ProductParameters
import pl.karol202.sciorder.server.table.Products

class ProductEntity(id: EntityID<Long>) : LongEntity(id), Mappable<Product>, Updatable<Product>
{
	companion object : LongEntityClass<ProductEntity>(Products)
	
	var store by StoreEntity referencedOn Products.storeId
	var name by Products.name
	var available by Products.available
	
	val parameters by ProductParameterEntity referrersOn ProductParameters.productId
	
	override fun map() = Product(id = id.value,
	                             storeId = store.id.value,
	                             name = name,
	                             available = available,
	                             parameters = parameters.map())
	
	override fun update(model: Product)
	{
		name = model.name
		available = model.available
		
		parameters.dispatchTo(model.parameters) {
			ProductParameterEntity.new { product = this@ProductEntity }
		}
	}
}
