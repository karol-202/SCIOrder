package pl.karol202.sciorder.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_product.*
import pl.karol202.sciorder.R
import pl.karol202.sciorder.extensions.ctx
import pl.karol202.sciorder.model.Product

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ViewHolder>()
{
	class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer
	{
		private val drawableExpandMoreToLess
				by lazy { AnimatedVectorDrawableCompat.create(containerView.ctx, R.drawable.anim_expand_more_to_less_white_24dp) }
		private val drawableExpandLessToMore
				by lazy { AnimatedVectorDrawableCompat.create(containerView.ctx, R.drawable.anim_expand_less_to_more_white_24dp) }

		private var expanded = false

		init
		{
			containerView.setOnClickListener { toggleExpand() }

			recyclerProductParams.layoutManager = LinearLayoutManager(containerView.ctx)
		}

		fun bind(product: Product)
		{
			expanded = false

			textProductName.text = product.name

			recyclerProductParams.adapter = ProductParamAdapter(product)

			updateExpand()
		}

		private fun toggleExpand()
		{
			expanded = !expanded
			updateExpand()
		}

		private fun updateExpand()
		{
			val animatedDrawable = if(expanded) drawableExpandMoreToLess else drawableExpandLessToMore
			imageProductExpand.setImageDrawable(animatedDrawable?.apply { start() })

			recyclerProductParams.visibility = if(expanded) View.VISIBLE else View.GONE
		}
	}

	private class DiffCallback(private val oldList: List<Product>,
	                           private val newList: List<Product>) : DiffUtil.Callback()
	{
		override fun getOldListSize() = oldList.size

		override fun getNewListSize() = newList.size

		override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
				oldList[oldItemPosition]._id == newList[newItemPosition]._id

		override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
				oldList[oldItemPosition] == newList[newItemPosition]
	}

	var products = emptyList<Product>()
		set(value)
		{
			val result = DiffUtil.calculateDiff(DiffCallback(field, value))
			field = value
			result.dispatchUpdatesTo(this)
		}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
	{
		val view = LayoutInflater.from(parent.ctx).inflate(R.layout.item_product, parent, false)
		return ViewHolder(view)
	}

	override fun getItemCount() = products.size

	override fun onBindViewHolder(holder: ViewHolder, position: Int)
	{
		holder.bind(products[position])
	}
}