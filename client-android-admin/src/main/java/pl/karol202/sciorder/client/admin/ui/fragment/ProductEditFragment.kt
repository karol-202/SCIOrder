package pl.karol202.sciorder.client.admin.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_product_edit.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import pl.karol202.sciorder.client.admin.R
import pl.karol202.sciorder.client.admin.viewmodel.ProductsViewModel

class ProductEditFragment : Fragment()
{
	private val productViewModel by sharedViewModel<ProductsViewModel>()

	private val arguments by navArgs<ProductEditFragmentArgs>()
	private val productId by lazy { arguments.productId }

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
			inflater.inflate(R.layout.fragment_product_edit, container, false)

	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		textView.text = productId
	}
}
