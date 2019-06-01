package pl.karol202.sciorder.client.android.common.extensions

import pl.karol202.sciorder.client.android.common.R
import pl.karol202.sciorder.common.model.Product

val Product.Parameter.Type.visibleName get() = when(this)
{
	Product.Parameter.Type.TEXT -> R.string.product_param_type_text
	Product.Parameter.Type.INT -> R.string.product_param_type_int
	Product.Parameter.Type.FLOAT -> R.string.product_param_type_float
	Product.Parameter.Type.BOOL -> R.string.product_param_type_bool
	Product.Parameter.Type.ENUM -> R.string.product_param_type_enum
}
