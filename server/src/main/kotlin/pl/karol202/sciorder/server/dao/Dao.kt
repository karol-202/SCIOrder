package pl.karol202.sciorder.server.dao

import pl.karol202.sciorder.model.Order
import pl.karol202.sciorder.model.Product

interface Dao
{
    suspend fun addOrder(order: Order)

    suspend fun getAllOrders(): List<Order>

    suspend fun addProduct(product: Product)

    suspend fun getAllProducts(): List<Product>

    suspend fun getProductOfId(id: String): Product?
}