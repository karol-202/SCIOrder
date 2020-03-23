package pl.karol202.sciorder.client.android.common.model

import pl.karol202.sciorder.client.android.common.R
import pl.karol202.sciorder.common.model.ProductParameter

val ProductParameter.Type.visibleName get() = when(this)
{
	ProductParameter.Type.TEXT -> R.string.product_param_type_text
	ProductParameter.Type.INT -> R.string.product_param_type_int
	ProductParameter.Type.FLOAT -> R.string.product_param_type_float
	ProductParameter.Type.BOOL -> R.string.product_param_type_bool
	ProductParameter.Type.ENUM -> R.string.product_param_type_enum
}
