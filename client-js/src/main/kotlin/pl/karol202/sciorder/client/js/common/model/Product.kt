package pl.karol202.sciorder.client.js.common.model

import pl.karol202.sciorder.common.Product

val Product.Parameter.Type.visibleName get() = when(this)
{
	Product.Parameter.Type.TEXT -> "Tekst"
	Product.Parameter.Type.INT -> "Liczba całkowita"
	Product.Parameter.Type.FLOAT -> "Liczba rzeczywista"
	Product.Parameter.Type.BOOL -> "Logiczny"
	Product.Parameter.Type.ENUM -> "Wyliczeniowy"
}
