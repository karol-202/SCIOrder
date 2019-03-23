package pl.karol202.sciorder.server.dao

import pl.karol202.sciorder.model.Order
import pl.karol202.sciorder.model.Product

interface Dao
{
    suspend fun createOrder(entries: List<Order.Entry>)

    suspend fun getAllOrders(): List<Order>

    suspend fun createProduct(name: String, available: Boolean, parameters: List<Product.Parameter>)

    suspend fun getAllProducts(): List<Product>

    suspend fun getProductOfId(id: String): Product?
}