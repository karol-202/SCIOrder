package pl.karol202.sciorder.common.request

import kotlinx.serialization.Serializable
import pl.karol202.sciorder.common.model.ProductParameter
import pl.karol202.sciorder.common.model.ProductParameter.Attributes
import pl.karol202.sciorder.common.model.ProductParameter.Type
import pl.karol202.sciorder.common.util.JvmSerializable

@Serializable
data class ProductParameterRequest(val name: String = "",
                                   val type: Type = Type.TEXT,
                                   val attributes: Attributes = Attributes()) : JvmSerializable
{
	fun toParameter(id: Long, productId: Long) = ProductParameter(id, productId, name, type, attributes)
}
