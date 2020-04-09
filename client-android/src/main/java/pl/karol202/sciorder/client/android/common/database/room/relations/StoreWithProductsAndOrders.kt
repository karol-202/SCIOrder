package pl.karol202.sciorder.client.android.common.database.room.relations

import androidx.room.Embedded
import androidx.room.Relation
import pl.karol202.sciorder.client.android.common.database.room.*
import pl.karol202.sciorder.client.android.common.database.room.entity.OrderEntity
import pl.karol202.sciorder.client.android.common.database.room.entity.ProductEntity
import pl.karol202.sciorder.client.android.common.database.room.entity.StoreEntity
import pl.karol202.sciorder.client.android.common.database.room.model.StoreWithSelection
import pl.karol202.sciorder.common.model.Store

data class StoreWithProductsAndOrders(@Embedded val store: StoreEntity,
                                      @Relation(entity = ProductEntity::class,
                                                parentColumn = "id",
                                                entityColumn = "storeId")
                                      val products: List<ProductWithParameters>,
                                      @Relation(entity = OrderEntity::class,
                                                parentColumn = "id",
                                                entityColumn = "storeId")
                                      val orders: List<OrderWithEntries>)
{
	companion object : ToModelMapper<StoreWithProductsAndOrders, Store>,
	                   ToEntityMapper<StoreWithProductsAndOrders, StoreWithSelection>
	{
		override fun toModel(entity: StoreWithProductsAndOrders) = with(entity) {
			Store(store.id, store.name, products.toModels(ProductWithParameters), orders.toModels(OrderWithEntries))
		}
		
		override fun toEntity(model: StoreWithSelection) =
				StoreWithProductsAndOrders(model.store.toEntity(StoreEntity.mapper(model.selected)),
				                           model.store.products.toEntities(ProductWithParameters),
				                           model.store.orders.toEntities(OrderWithEntries))
	}
}

val List<StoreWithProductsAndOrders>.stores get() = map { it.store }
val List<StoreWithProductsAndOrders>.products get() = flatMap { it.products }.products
val List<StoreWithProductsAndOrders>.productsParameters get() = flatMap { it.products }.parameters
val List<StoreWithProductsAndOrders>.productsEnumValues get() = flatMap { it.products }.enumValues
val List<StoreWithProductsAndOrders>.orders get() = flatMap { it.orders }.orders
val List<StoreWithProductsAndOrders>.ordersEntries get() = flatMap { it.orders }.entries
val List<StoreWithProductsAndOrders>.ordersParameterValues get() = flatMap { it.orders }.parameterValues
