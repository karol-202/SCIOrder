package pl.karol202.sciorder.client.android.common.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import pl.karol202.sciorder.common.model.Product

@Entity(primaryKeys = [ "productId", "ordinal" ],
        foreignKeys = [ ForeignKey(entity = ProductEntity::class,
                                   parentColumns = [ "id" ], childColumns = [ "productId" ],
                                   onUpdate = ForeignKey.CASCADE, onDelete = ForeignKey.CASCADE) ])
data class ProductParameterEntity(val productId: String,
                                  val ordinal: Int,
                                  val name: String,
                                  val type: Product.Parameter.Type,
                                  @Embedded val attributes: Product.Parameter.Attributes)
