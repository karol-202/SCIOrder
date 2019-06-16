package pl.karol202.sciorder.client.android.user.ui.dialog.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import pl.karol202.sciorder.client.android.user.ui.dialog.ProductOrderDialog
import pl.karol202.sciorder.client.android.user.ui.listener.OnProductOrderListener
import pl.karol202.sciorder.client.common.components.ExtendedDialogFragment
import pl.karol202.sciorder.client.common.extensions.ctx
import pl.karol202.sciorder.client.common.extensions.setArguments
import pl.karol202.sciorder.client.common.extensions.setTargetFragment
import pl.karol202.sciorder.client.common.extensions.to
import pl.karol202.sciorder.common.Product

class ProductOrderDialogFragment : ExtendedDialogFragment()
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
