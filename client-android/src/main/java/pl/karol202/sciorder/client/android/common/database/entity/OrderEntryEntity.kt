package pl.karol202.sciorder.client.android.common.database.entity

data class OrderEntryEntity(val orderId: String,
                            val ordinal: Int,
                            val productId: String,
                            val quantity: Int)
