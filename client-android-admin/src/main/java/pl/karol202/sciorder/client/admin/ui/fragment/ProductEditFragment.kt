package pl.karol202.sciorder.client.admin.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_product_edit.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import pl.karol202.sciorder.client.admin.R
import pl.karol202.sciorder.client.admin.ui.adapter.ProductParamAdapter
import pl.karol202.sciorder.client.admin.viewmodel.ProductsViewModel
import pl.karol202.sciorder.client.common.components.ExtendedFragment
import pl.karol202.sciorder.client.common.extensions.*
import pl.karol202.sciorder.common.model.Product

class ProductEditFragment : ExtendedFragment()
{
	private val productsViewModel by sharedViewModel<ProductsViewModel>()

	private val arguments by navArgs<ProductEditFragmentArgs>()
	private val productId by lazy { arguments.productId }

	private val adapter = ProductParamAdapter { savedParameters = it }

	private var savedParameters by instanceState<List<Product.Parameter>>()

	override val layoutRes = R.layout.fragment_orders

	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		initNameEditText()
		initParamsRecycler()
		initApplyButton()

		observeUpdateEvent()

		productId?.let { observeProductsAndPopulateViews(it) }
	}

	private fun initNameEditText()
	{
		fun checkNameValidity(name: String?)
		{
			editLayoutProductEditName.error =
					if(name.isNullOrBlank()) ctx.getString(R.string.text_product_edit_name_no_value) else ""
		}

		editTextProductEditName.addAfterTextChangedListener { checkNameValidity(it) }
		checkNameValidity(null)
	}

	private fun initParamsRecycler()
	{
		recyclerProductEditParams.layoutManager = LinearLayoutManager(ctx)
		recyclerProductEditParams.isNestedScrollingEnabled = false
		recyclerProductEditParams.adapter = adapter
	}

	private fun initApplyButton()
	{
		buttonProductEditApply.setOnClickListener { apply() }
	}

	private fun observeUpdateEvent()
	{
		productsViewModel.updateEventLiveData.observeEvent(viewLifecycleOwner) {
			if(it == ProductsViewModel.UpdateResult.SUCCESS) navigateBack()
			else showSnackbar(R.string.text_update_error)
		}
	}

	private fun observeProductsAndPopulateViews(productId: String)
	{
		productsViewModel.productsLiveData.observeOnceNonNull { products ->
			val product = products.find { it.id == productId } ?: throw IllegalArgumentException()
			populateViews(product)
		}
	}

	private fun populateViews(product: Product)
	{
		editTextProductEditName.setText(product.name)

		checkProductEditAvailable.isChecked = product.available

		adapter.parameters = product.parameters
	}

	override fun onRestoreInstanceState()
	{
		adapter.parameters = savedParameters ?: emptyList()
	}

	private fun apply()
	{
		val product = createProduct() ?: return showSnackbar(R.string.text_product_edit_error)
		if(productId != null) productsViewModel.updateProduct(product)
		else productsViewModel.addProduct(product)
	}

	private fun createProduct(): Product?
	{
		val name = getName() ?: return null
		val available = getAvailable()
		val parameters = getParameters() ?: return null
		return Product(productId ?: "", "", name, available, parameters)
	}

	private fun getName() = editTextProductEditName.text?.toString()?.takeIf { it.isNotBlank() }

	private fun getAvailable() = checkProductEditAvailable.isChecked

	private fun getParameters() = adapter.parameters.takeIf { it.allValid() }

	private fun List<Product.Parameter>.allValid() = all { it.isValid() }

	private fun Product.Parameter.isValid() = name.isNotBlank()

	private fun navigateBack() = fragmentManager?.popBackStack()
}
