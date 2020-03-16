package pl.karol202.sciorder.client.android.common.database.room.converter

import androidx.room.TypeConverter
import pl.karol202.sciorder.common.model.ProductParameter

class ProductParameterTypeConverter
{
	@TypeConverter
	fun fromType(type: ProductParameter.Type) = type.name
	
	@TypeConverter
	fun toType(name: String) = ProductParameter.Type.getByName(name)
}
