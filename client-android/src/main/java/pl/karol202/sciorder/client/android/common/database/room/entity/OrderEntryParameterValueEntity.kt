package pl.karol202.sciorder.client.android.common.database.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import pl.karol202.sciorder.common.util.IdProvider

@Entity(foreignKeys =
        [
			ForeignKey(entity = OrderEntryEntity::class,
			           parentColumns = [ "id" ],
			           childColumns = [ "orderEntryId" ],
			           onUpdate = ForeignKey.CASCADE,
			           onDelete = ForeignKey.CASCADE),
			ForeignKey(entity = ProductParameterEntity::class,
			           parentColumns = [ "id" ],
			           childColumns = [ "productParameterId" ],
			           onUpdate = ForeignKey.CASCADE,
			           onDelete = ForeignKey.CASCADE)
		],
        indices = [Index("orderEntryId"), Index("productParameterId")])
data class OrderEntryParameterValueEntity(@PrimaryKey(autoGenerate = true) override val id: Long,
                                          val orderEntryId: Long,
                                          val productParameterId: Long,
                                          val value: String) : IdProvider<Long>
