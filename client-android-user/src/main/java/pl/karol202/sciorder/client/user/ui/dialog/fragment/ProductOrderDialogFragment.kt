package pl.karol202.sciorder.client.user.ui.dialog.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import pl.karol202.sciorder.client.user.components.ExtendedDialogFragment
import pl.karol202.sciorder.client.user.extensions.ctx
import pl.karol202.sciorder.client.user.extensions.setArguments
import pl.karol202.sciorder.client.user.extensions.setTargetFragment
import pl.karol202.sciorder.client.user.extensions.to
import pl.karol202.sciorder.common.model.Product
import pl.karol202.sciorder.client.user.ui.listeners.OnProductOrderListener
import pl.karol202.sciorder.client.user.ui.dialog.ProductOrderDialog

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
