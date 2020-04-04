package pl.karol202.sciorder.common.request

import kotlinx.serialization.Serializable
import pl.karol202.sciorder.common.util.JvmSerializable

interface ProductRequest
{
	val name: String
	val available: Boolean
}

@Serializable
data class ProductCreateRequest(override val name: String,
                                override val available: Boolean,
                                val createdParameters: List<ProductParameterRequest>) : ProductRequest,
                                                                                        JvmSerializable

@Serializable
data class ProductUpdateRequest(override val name: String,
                                override val available: Boolean,
                                val createdParameters: List<ProductParameterRequest>,
                                val updatedParameters: Map<Long, ProductParameterRequest>,
                                val removedParameters: List<Long>) : ProductRequest, JvmSerializable
