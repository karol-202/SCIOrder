package pl.karol202.sciorder.client.common.model

import pl.karol202.sciorder.common.model.Store

data class StoreInfo(val id: Long,
                     val name: String,
                     val productsAmount: Int,
                     val activeOrdersAmount: Int)

val Store.storeInfo get() = StoreInfo(id, name, products.size, orders.count { it.isActive })
