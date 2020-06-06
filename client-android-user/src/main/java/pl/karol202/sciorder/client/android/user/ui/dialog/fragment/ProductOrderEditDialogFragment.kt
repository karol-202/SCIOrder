package pl.karol202.sciorder.client.android.user.ui.dialog.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import pl.karol202.sciorder.client.android.common.component.DialogFragmentWithArguments
import pl.karol202.sciorder.client.android.common.util.ctx
import pl.karol202.sciorder.client.android.common.util.setTargetFragment
import pl.karol202.sciorder.client.android.common.util.set
import pl.karol202.sciorder.client.android.user.ui.dialog.ProductOrderEditDialog
import pl.karol202.sciorder.client.android.user.ui.listener.OnProductOrderEditListener
import pl.karol202.sciorder.client.common.model.OrderedProduct

class ProductOrderEditDialogFragment : DialogFragmentWithArguments()
{
	companion object
	{
		fun <L> create(orderedProduct: OrderedProduct, orderListener: L)
				where L : Fragment,
				      L : OnProductOrderEditListener =
			ProductOrderEditDialogFragment().setArguments(ProductOrderEditDialogFragment::orderedProduct set orderedProduct)
					.setTargetFragment(orderListener)
	}

	private val orderedProduct by argumentsOrThrow<OrderedProduct>()
	private val orderListener by lazy { targetFragment as OnProductOrderEditListener }

	override fun onCreateDialog(savedInstanceState: Bundle?) = ProductOrderEditDialog(ctx, orderedProduct, orderListener)
}
