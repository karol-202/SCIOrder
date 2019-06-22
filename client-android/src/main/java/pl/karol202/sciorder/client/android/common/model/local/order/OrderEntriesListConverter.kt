package pl.karol202.sciorder.client.android.common.model.local.order

import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import pl.karol202.sciorder.common.Order

class OrderEntriesListConverter
{
    private val entriesListType = Types.newParameterizedType(List::class.java, Order.Entry::class.java)
    private val adapter = Moshi.Builder().build().adapter<List<Order.Entry>>(entriesListType)

    @TypeConverter
    fun fromEntriesList(entries: List<Order.Entry>): String = adapter.toJson(entries)

    @TypeConverter
    fun toEntriesList(data: String) = adapter.fromJson(data)
}