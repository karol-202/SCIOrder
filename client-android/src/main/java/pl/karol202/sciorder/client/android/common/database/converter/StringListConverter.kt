package pl.karol202.sciorder.client.android.common.database.converter

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json
import kotlinx.serialization.list
import kotlinx.serialization.serializer

class StringListConverter
{
	@TypeConverter
	fun fromStringList(list: List<String>) = Json.stringify(String.serializer().list, list)
	
	@TypeConverter
	fun toStringList(json: String) = Json.parse(String.serializer().list, json)
}
