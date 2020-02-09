package pl.karol202.sciorder.client.android.common.database.order

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json
import kotlinx.serialization.list
import pl.karol202.sciorder.common.model.Order

class OrderEntriesListConverter
{
    @TypeConverter
    fun fromEntriesList(entries: List<Order.Entry>) = Json.stringify(Order.Entry.serializer().list, entries)

    @TypeConverter
    fun toEntriesList(data: String) = Json.parse(Order.Entry.serializer().list, data)
}
