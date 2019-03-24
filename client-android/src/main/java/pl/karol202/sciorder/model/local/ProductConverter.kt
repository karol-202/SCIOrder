package pl.karol202.sciorder.model.local

import androidx.room.TypeConverter
import pl.karol202.sciorder.model.Product

class ProductConverter
{
	@TypeConverter
	fun fromProductEntity(productEntity: ProductEntity) =
			Product(productEntity.id,
					productEntity.name,
					productEntity.available,
					productEntity.parameters)

	@TypeConverter
	fun toProductEntity(product: Product) =
			ProductEntity(product._id, product.name, product.available, product.parameters)
}