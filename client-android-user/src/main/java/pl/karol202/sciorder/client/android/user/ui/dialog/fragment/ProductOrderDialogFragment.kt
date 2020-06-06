package pl.karol202.sciorder.client.android.user.ui.dialog.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import pl.karol202.sciorder.client.android.common.component.DialogFragmentWithArguments
import pl.karol202.sciorder.client.android.common.util.ctx
import pl.karol202.sciorder.client.android.common.util.setTargetFragment
import pl.karol202.sciorder.client.android.user.ui.dialog.ProductOrderDialog
import pl.karol202.sciorder.client.android.user.ui.listener.OnProductOrderListener
import pl.karol202.sciorder.common.model.Product

class ProductOrderDialogFragment : DialogFragmentWithArguments()
{
	companion object
	{
		fun <L> create(product: Product, orderListener: L)
				where L : Fragment,
				      L : OnProductOrderListener =
			ProductOrderDialogFragment().setArguments(ProductOrderDialogFragment::product to product)
					.setTargetFragment(orderListener)
	}

	private val product by argumentsOrThrow<Product>()
	private val orderListener by lazy { targetFragment as OnProductOrderListener }

	override fun onCreateDialog(savedInstanceState: Bundle?) = ProductOrderDialog(ctx, product, orderListener)
}
