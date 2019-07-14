package pl.karol202.sciorder.client.js.common.model

import com.ccfraser.muirwik.components.Colors
import kotlinx.css.Color
import pl.karol202.sciorder.common.Order

val Order.Status.visibleName get() = when(this)
{
	Order.Status.WAITING -> "Oczekiwanie"
	Order.Status.IN_PROGRESS -> "Przygotowywanie"
	Order.Status.DELIVERING -> "Dostarczanie"
	Order.Status.DONE -> "Dostarczono"
	Order.Status.REJECTED -> "Odrzucono"
}

val Order.Status.color get() = when(this)
{
	Order.Status.WAITING -> Colors.Yellow.shade500
	Order.Status.IN_PROGRESS -> Colors.Yellow.shade500
	Order.Status.DELIVERING -> Colors.LightGreen.shade500
	Order.Status.DONE -> Colors.LightGreen.shade500
	Order.Status.REJECTED -> Color("#FF1500")
}
