package pl.karol202.sciorder.model.local

import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import pl.karol202.sciorder.model.Product

class ProductParametersListConverter
{
    @TypeConverter
    fun fromParametersList(parameters: List<Product.Parameter>) = getParameterListAdapter().toJson(parameters)

    @TypeConverter
    fun toParametersList(data: String) = getParameterListAdapter().fromJson(data)

    private fun getParameterListAdapter() = Moshi.Builder().build().adapter<List<Product.Parameter>>(getParameterListType())

    private fun getParameterListType() = Types.newParameterizedType(List::class.java, Product.Parameter::class.java)
}