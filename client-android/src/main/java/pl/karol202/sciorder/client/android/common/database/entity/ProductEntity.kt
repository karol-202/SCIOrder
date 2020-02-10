package pl.karol202.sciorder.client.android.common.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ProductEntity(@PrimaryKey val id: String,
                         val name: String,
                         val available: Boolean)
