package pl.karol202.sciorder.server.dao.impl

import org.jetbrains.exposed.sql.*
import pl.karol202.sciorder.common.model.Product
import pl.karol202.sciorder.server.dao.ProductDao
import pl.karol202.sciorder.server.entity.ProductParameterEnumValueTable
import pl.karol202.sciorder.server.entity.ProductParameterTable
import pl.karol202.sciorder.server.entity.ProductTable

class ProductDaoImpl : ProductDao
{
	override suspend fun insertProduct(product: Product): Product
	{
		val productId = ProductTable.insertAndGetId {
			it[storeId] = product.storeId
			it[name] = product.name
			it[available] = product.available
		}.value
		
		product.parameters.forEachIndexed { ordinal, parameter -> insertProductParameter(productId, ordinal, parameter) }
	}
	
	private fun insertProductParameter(targetProductId: Int, parameterOrdinal: Int, parameter: Product.Parameter)
	{
		val parameterId = ProductParameterTable.insertAndGetId {
			it[productId] = targetProductId
			it[ordinal] = parameterOrdinal
			it[name] = parameter.name
			it[type] = parameter.type
			it[minimalValue] = parameter.attributes.minimalValue
			it[maximalValue] = parameter.attributes.maximalValue
			it[defaultValue] = parameter.attributes.defaultValue
		}.value
		
		parameter.attributes.enumValues?.forEachIndexed { enumOrdinal, enumValue ->
			insertProductParameterEnumValue(parameterId, enumOrdinal, enumValue)
		}
	}
	
	private fun insertProductParameterEnumValue(parameterId: Int, enumOrdinal: Int, enumValue: String)
	{
		ProductParameterEnumValueTable.insert {
			it[productParameterId] = parameterId
			it[ordinal] = enumOrdinal
			it[value] = enumValue
		}
	}
	
	override suspend fun updateProduct(product: Product)
	{
		ProductTable.update({ ProductTable.id eq product.id }) {
			it[storeId] = product.storeId
			it[name] = product.name
			it[available] = product.available
		}
		
		//ProductParameterTable.
	}
	
	override suspend fun deleteProduct(productId: Int)
	{
		ProductTable.deleteWhere { ProductTable.id eq productId }
	}
	
	override suspend fun getProductsByStore(storeId: Int): List<Product>
	{
		return (ProductTable leftJoin ProductParameterTable leftJoin ProductParameterEnumValueTable)
				.select { ProductTable.storeId eq storeId }
				.toList()
				.getProducts()
	}
	
	override suspend fun getProductById(productId: Int): Product?
	{
		return (ProductTable leftJoin ProductParameterTable leftJoin ProductParameterEnumValueTable)
				.select { ProductTable.id eq productId }
				.toList()
				.getProducts()
				.singleOrNull()
	}
	
	private fun List<ResultRow>.getProducts() = this
			.groupBy { Product(id = it[ProductTable.id].value,
			                   storeId = it[ProductTable.storeId],
			                   name = it[ProductTable.name],
			                   available = it[ProductTable.available],
			                   parameters = emptyList()) }
			.map { (product, rows) ->
				product.copy(parameters = rows.getProductParameters())
			}
	
	private fun List<ResultRow>.getProductParameters() = this
			.groupBy { Product.Parameter(name = it[ProductParameterTable.name],
			                             type = it[ProductParameterTable.type],
			                             attributes = Product.Parameter.Attributes(
					                             minimalValue = it[ProductParameterTable.minimalValue],
					                             maximalValue = it[ProductParameterTable.maximalValue],
					                             enumValues = emptyList(),
					                             defaultValue = it[ProductParameterTable.defaultValue])) to
					it[ProductParameterTable.ordinal] }
			.map { (parameterAndOrdinal, rows) ->
				val (parameter, ordinal) = parameterAndOrdinal
				val newAttributes = parameter.attributes.copy(enumValues = rows.getProductParameterEnumValues())
				parameter.copy(attributes = newAttributes) to ordinal
			}
			.sortedBy { (_, ordinal) -> ordinal }
			.map { (parameter, _) -> parameter }
	
	private fun List<ResultRow>.getProductParameterEnumValues() = this
			.sortedBy { it[ProductParameterEnumValueTable.ordinal] }
			.map { it[ProductParameterEnumValueTable.value] }
	
	/*private val productsCollection = database.getCollection<Product>()

	override suspend fun insertProduct(product: Product)
	{
		productsCollection.insertOne(product)
	}

	override suspend fun updateProduct(ownerId: String, product: Product) =
			productsCollection.updateOne(and(Product::ownerId eq ownerId, Product::_id eq product.id), product)
					.let { it.wasAcknowledged() && it.matchedCount > 0 }

	override suspend fun deleteProduct(ownerId: String, id: String) =
			productsCollection.deleteOne(Product::ownerId eq ownerId, Product::_id eq id)
					.let { it.wasAcknowledged() && it.deletedCount > 0 }

	override suspend fun getProductsByOwner(ownerId: String) =
			productsCollection.find(Product::ownerId eq ownerId).toList()

	override suspend fun getProductById(ownerId: String, id: String) =
			productsCollection.findOne(Product::ownerId eq ownerId, Product::_id eq id)*/
}
