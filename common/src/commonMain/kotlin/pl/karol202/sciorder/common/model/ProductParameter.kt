package pl.karol202.sciorder.common.model

import kotlinx.serialization.Serializable
import pl.karol202.sciorder.common.util.IdProvider
import pl.karol202.sciorder.common.util.JvmSerializable

@Serializable
data class ProductParameter(override val id: Long,
                            val productId: Long,
                            val name: String,
                            val type: Type,
                            val attributes: Attributes) : JvmSerializable, IdProvider<Long>
{
	enum class Type
	{
		TEXT, INT, FLOAT, BOOL, ENUM;
		
		companion object
		{
			fun getByName(name: String) = values().find { it.name == name }
		}
	}
	
	@Serializable
	data class Attributes(val minimalValue: Float? = null,
	                      val maximalValue: Float? = null,
	                      val enumValues: List<String>? = null,
	                      val defaultValue: String? = null) : JvmSerializable
}
