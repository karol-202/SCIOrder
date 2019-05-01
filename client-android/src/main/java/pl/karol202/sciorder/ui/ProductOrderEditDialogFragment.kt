package pl.karol202.sciorder.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import pl.karol202.sciorder.components.ExtendedDialogFragment
import pl.karol202.sciorder.extensions.ctx
import pl.karol202.sciorder.extensions.setArguments
import pl.karol202.sciorder.extensions.setTargetFragment
import pl.karol202.sciorder.extensions.to
import pl.karol202.sciorder.model.OrderedProduct
import pl.karol202.sciorder.model.Product

class ProductOrderEditDialogFragment : ExtendedDialogFragment()
{
	companion object
	{
		fun <L> create(orderedProduct: OrderedProduct, orderListener: L)
				where L : Fragment,
				      L : OnProductOrderEditListener =
			ProductOrderEditDialogFragment().setArguments(ProductOrderEditDialogFragment::orderedProduct to orderedProduct)
					.setTargetFragment(orderListener)
	}

	private val orderedProduct by argumentsOrThrow<OrderedProduct>()
	private val orderListener by lazy { targetFragment as? OnProductOrderEditListener }

	override fun onCreateDialog(savedInstanceState: Bundle?) = ProductOrderEditDialog(ctx, orderedProduct, orderListener)
}