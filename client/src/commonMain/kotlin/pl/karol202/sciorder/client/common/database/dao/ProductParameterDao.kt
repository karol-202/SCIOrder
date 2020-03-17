package pl.karol202.sciorder.client.common.database.dao

import kotlinx.coroutines.flow.Flow
import pl.karol202.sciorder.common.model.ProductParameter

interface ProductParameterDao : InsertDao<ProductParameter>, UpdateDao<ProductParameter>, DeleteDao<ProductParameter>
{
	fun getById(parameterId: Long): Flow<ProductParameter?>
}

