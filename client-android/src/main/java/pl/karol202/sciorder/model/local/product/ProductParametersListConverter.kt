package pl.karol202.sciorder.model.local.product

import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import pl.karol202.sciorder.model.Product

class ProductParametersListConverter
{
    private val parametersListType = Types.newParameterizedType(List::class.java, Product.Parameter::class.java)
    private val adapter = Moshi.Builder().build().adapter<List<Product.Parameter>>(parametersListType)

    @TypeConverter
    fun fromParametersList(parameters: List<Product.Parameter>): String = adapter.toJson(parameters)

    @TypeConverter
    fun toParametersList(data: String) = adapter.fromJson(data)
}