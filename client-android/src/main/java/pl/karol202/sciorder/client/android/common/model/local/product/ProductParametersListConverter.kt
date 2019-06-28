package pl.karol202.sciorder.client.android.common.model.local.product

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json
import kotlinx.serialization.list
import pl.karol202.sciorder.common.Product

class ProductParametersListConverter
{
    @TypeConverter
    fun fromParametersList(parameters: List<Product.Parameter>) = Json.stringify(Product.Parameter.serializer().list, parameters)

    @TypeConverter
    fun toParametersList(data: String) = Json.parse(Product.Parameter.serializer().list, data)
}
