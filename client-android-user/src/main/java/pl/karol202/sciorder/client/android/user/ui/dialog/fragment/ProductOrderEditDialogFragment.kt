package pl.karol202.sciorder.client.android.user.ui.dialog.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import pl.karol202.sciorder.client.android.common.components.ExtendedDialogFragment
import pl.karol202.sciorder.client.android.common.extensions.ctx
import pl.karol202.sciorder.client.android.common.extensions.setArguments
import pl.karol202.sciorder.client.android.common.extensions.setTargetFragment
import pl.karol202.sciorder.client.android.common.extensions.to
import pl.karol202.sciorder.client.android.common.model.OrderedProduct
import pl.karol202.sciorder.client.android.user.ui.dialog.ProductOrderEditDialog
import pl.karol202.sciorder.client.android.user.ui.listener.OnProductOrderEditListener

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
	private val orderListener by lazy { targetFragment as OnProductOrderEditListener }

	override fun onCreateDialog(savedInstanceState: Bundle?) = ProductOrderEditDialog(ctx, orderedProduct, orderListener)
}
