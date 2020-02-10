package pl.karol202.sciorder.client.android.common.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(primaryKeys = [ "orderEntryId", "productId", "productParameterOrdinal" ],
        foreignKeys = [
	        ForeignKey(entity = OrderEntryEntity::class,
	                   parentColumns = [ "id" ], childColumns = [ "orderEntryId" ],
	                   onUpdate = ForeignKey.CASCADE, onDelete = ForeignKey.CASCADE),
	        ForeignKey(entity = ProductParameterEntity::class,
	                   parentColumns = [ "productId", "ordinal" ], childColumns = [ "productId", "productParameterOrdinal" ],
	                   onUpdate = ForeignKey.CASCADE, onDelete = ForeignKey.CASCADE)
        ])
data class OrderEntryParameterValueEntity(val orderEntryId: String,
                                          val productId: String,
                                          val productParameterOrdinal: Int,
                                          val value: String)
