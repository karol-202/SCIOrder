package pl.karol202.sciorder.ui.dialog.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import pl.karol202.sciorder.components.ExtendedDialogFragment
import pl.karol202.sciorder.extensions.ctx
import pl.karol202.sciorder.extensions.setArguments
import pl.karol202.sciorder.extensions.setTargetFragment
import pl.karol202.sciorder.extensions.to
import pl.karol202.sciorder.model.Product
import pl.karol202.sciorder.ui.listeners.OnProductOrderListener
import pl.karol202.sciorder.ui.dialog.ProductOrderDialog

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
