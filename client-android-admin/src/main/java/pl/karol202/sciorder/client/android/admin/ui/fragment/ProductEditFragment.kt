package pl.karol202.sciorder.client.android.admin.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_product_edit.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import pl.karol202.sciorder.client.android.admin.R
import pl.karol202.sciorder.client.android.admin.ui.adapter.ProductParamAdapter
import pl.karol202.sciorder.client.android.common.component.ExtendedFragment
import pl.karol202.sciorder.client.android.common.ui.addAfterTextChangedListener
import pl.karol202.sciorder.client.android.common.util.ctx
import pl.karol202.sciorder.client.android.common.util.observeEvent
import pl.karol202.sciorder.client.android.common.util.showSnackbar
import pl.karol202.sciorder.client.android.common.viewmodel.ProductsEditAndroidViewModel
import pl.karol202.sciorder.client.common.model.create
import pl.karol202.sciorder.client.common.viewmodel.AdminProductsViewModel
import pl.karol202.sciorder.common.model.Product

class ProductEditFragment : ExtendedFragment()
{
	private val productsViewModel by sharedViewModel<ProductsEditAndroidViewModel>()

	private val arguments by navArgs<ProductEditFragmentArgs>()
	private val initialProduct by lazy { arguments.product }

	private var product by instanceState { initialProduct ?: Product.create() }
	private var name
		get() = product.name
		set(value) = updateProduct(product.copy(name = value))
	private var available
		get() = product.available
		set(value) = updateProduct(product.copy(available = value))
	private var parameters
		get() = product.parameters
		set(value) = updateProduct(product.copy(parameters = value))

	override val layoutRes = R.layout.fragment_product_edit

	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		initNameEditText()
		initAvailabilityCheckbox()
		initParamsRecycler()
		initApplyButton()

		observeUpdateEvent()
	}

	private fun initNameEditText()
	{
		editTextProductEditName.setText(name)
		editTextProductEditName.addAfterTextChangedListener { name = it }
	}
	
	private fun initAvailabilityCheckbox()
	{
		checkProductEditAvailable.isChecked = available
		checkProductEditAvailable.setOnCheckedChangeListener { _, checked -> available = checked }
	}

	private fun initParamsRecycler()
	{
		recyclerProductEditParams.layoutManager = LinearLayoutManager(ctx)
		recyclerProductEditParams.isNestedScrollingEnabled = false
		recyclerProductEditParams.adapter = ProductParamAdapter(parameters) { parameters = it }
	}

	private fun initApplyButton()
	{
		buttonProductEditApply.setOnClickListener { apply() }
	}

	private fun observeUpdateEvent()
	{
		productsViewModel.updateEventLiveData.observeEvent(viewLifecycleOwner) {
			if(it == AdminProductsViewModel.UpdateResult.SUCCESS) navigateBack()
			else showSnackbar(R.string.text_update_error)
		}
	}

	private fun apply()
	{
		if(!product.isValid) return showSnackbar(R.string.text_product_edit_error)
		if(initialProduct != null) productsViewModel.updateProduct(product)
		else productsViewModel.addProduct(product)
	}

	private fun navigateBack() = fragmentManager?.popBackStack()
	
	private fun updateProduct(product: Product)
	{
		this.product = product
		validate()
	}
	
	private fun validate()
	{
		editLayoutProductEditName.error =
				if(!product.isNameValid) ctx.getString(R.string.text_product_edit_name_no_value) else null
	}
}
