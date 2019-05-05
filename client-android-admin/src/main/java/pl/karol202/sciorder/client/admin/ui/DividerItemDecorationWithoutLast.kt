package pl.karol202.sciorder.client.admin.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView

class DividerItemDecorationWithoutLast(context: Context) : RecyclerView.ItemDecoration()
{
	private val drawable: Drawable

	init
	{
		val typedArray = context.obtainStyledAttributes(intArrayOf(android.R.attr.listDivider))
		drawable = typedArray.getDrawable(0) ?: throw IllegalStateException("listDivider drawable not set for this theme.")
		typedArray.recycle()
	}

	override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State)
	{
		val left = parent.paddingLeft
		val right = parent.width - parent.paddingRight

		parent.children.forEachIndexed { index, child ->
			if(index == parent.childCount - 1) return@forEachIndexed

			val params = child.layoutParams as RecyclerView.LayoutParams
			val top = child.bottom + params.bottomMargin
			val bottom = top + drawable.intrinsicHeight
			drawable.setBounds(left, top, right, bottom)
			drawable.draw(canvas)
		}
	}
}
