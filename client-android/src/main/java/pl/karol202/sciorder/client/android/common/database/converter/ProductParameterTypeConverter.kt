package pl.karol202.sciorder.client.android.common.database.converter

import androidx.room.TypeConverter
import pl.karol202.sciorder.common.model.Product

class ProductParameterTypeConverter
{
	@TypeConverter
	fun fromType(type: Product.Parameter.Type) = type.name
	
	@TypeConverter
	fun toType(name: String) = Product.Parameter.Type.getByName(name)
}
