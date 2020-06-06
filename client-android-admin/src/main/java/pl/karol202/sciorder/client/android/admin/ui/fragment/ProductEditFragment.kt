package pl.karol202.sciorder.client.android.admin.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.observe
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_product_edit.*
import kotlinx.android.synthetic.main.toolbar.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import pl.karol202.sciorder.client.android.admin.R
import pl.karol202.sciorder.client.android.admin.ui.activity.ToolbarActivity
import pl.karol202.sciorder.client.android.admin.ui.adapter.ProductParamAdapter
import pl.karol202.sciorder.client.android.common.ui.fragment.InflatedFragment
import pl.karol202.sciorder.client.android.common.ui.setTextIfDiffer
import pl.karol202.sciorder.client.android.common.ui.simpleItemAnimator
import pl.karol202.sciorder.client.android.common.util.ctx
import pl.karol202.sciorder.client.android.common.util.inflateTransition
import pl.karol202.sciorder.client.android.common.util.observeEvent
import pl.karol202.sciorder.client.android.common.util.showSnackbar
import pl.karol202.sciorder.client.android.common.viewmodel.AdminProductEditAndroidViewModel
import pl.karol202.sciorder.client.common.viewmodel.AdminProductEditViewModel.NameValidationError
import pl.karol202.sciorder.client.common.viewmodel.AdminProductEditViewModel.UpdateError
import pl.karol202.sciorder.common.model.Product
import pl.karol202.sciorder.common.request.ProductRequest
import pl.karol202.sciorder.common.validation.MAX_NAME_LENGTH

class ProductEditFragment : InflatedFragment()
{
	private val viewModel by sharedViewModel<AdminProductEditAndroidViewModel>()
	
	private val navController by lazy { NavHostFragment.findNavController(this) }

	private val adapter = ProductParamAdapter(onParamAdd = { viewModel.addParameter() },
	                                          onParamEdit = { viewModel.updateParameter(it) },
	                                          onParamRemove = { viewModel.removeParameter(it) })
	
	override val layoutRes = R.layout.fragment_product_edit
	
	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		sharedElementEnterTransition = ctx.inflateTransition(android.R.transition.move)
		enterTransition = ctx.inflateTransition(android.R.transition.fade)
		exitTransition = ctx.inflateTransition(android.R.transition.fade)
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		handleBackPress()
		
		initToolbar()
		initNameEditText()
		initAvailabilityCheckbox()
		initParamsRecycler()
		initApplyButton()

		observeEditedProduct()
		observeEditedParameters()
		observeNameValidationError()
		observeUpdateErrorEvent()
	}
	
	private fun handleBackPress() = requireActivity().onBackPressedDispatcher.addCallback(this) {
		viewModel.stopEditing()
	}
	
	private fun initToolbar() = (activity as? ToolbarActivity)?.setToolbar(toolbar)

	private fun initNameEditText()
	{
		editLayoutProductEditName.counterMaxLength = Product.MAX_NAME_LENGTH
		editTextProductEditName.doAfterTextChanged { viewModel.updateProductName(it.toString()) }
	}
	
	private fun initAvailabilityCheckbox()
	{
		checkProductEditAvailable.setOnCheckedChangeListener { _, checked -> viewModel.updateProductAvailability(checked) }
	}

	private fun initParamsRecycler()
	{
		recyclerProductEditParams.layoutManager = LinearLayoutManager(ctx)
		recyclerProductEditParams.adapter = adapter
		recyclerProductEditParams.simpleItemAnimator?.supportsChangeAnimations = false
	}

	private fun initApplyButton()
	{
		buttonProductEditApply.setOnClickListener { viewModel.applyProduct() }
	}
	
	private fun observeEditedProduct() = viewModel.editedProductLiveData.observe(viewLifecycleOwner) {
		if(it != null) populateWithProduct(it)
		else navigateBack()
	}
	
	private fun populateWithProduct(product: ProductRequest)
	{
		editTextProductEditName.setTextIfDiffer(product.name)
		checkProductEditAvailable.isChecked = product.available
	}

	private fun navigateBack() = navController.popBackStack()
	
	private fun observeEditedParameters() = viewModel.editedParametersLiveData.observe(viewLifecycleOwner) {
		adapter.parameters = it
	}
	
	private fun observeNameValidationError() = viewModel.validationErrorLiveData.observe(viewLifecycleOwner) {
		editLayoutProductEditName.error = getNameValidationErrorText(it)
	}
	
	private fun getNameValidationErrorText(error: NameValidationError?) = when(error)
	{
		NameValidationError.NAME_BLANK -> ctx.getString(R.string.text_product_edit_name_no_value)
		NameValidationError.NAME_TOO_LONG -> ctx.getString(R.string.text_product_edit_name_too_long)
		else -> null
	}
	
	private fun observeUpdateErrorEvent() = viewModel.updateErrorEventLiveData.observeEvent(viewLifecycleOwner) {
		showSnackbar(getUpdateErrorText(it))
	}
	
	private fun getUpdateErrorText(error: UpdateError) = when(error)
	{
		UpdateError.NETWORK -> R.string.text_network_error
		UpdateError.PRODUCT_INVALID -> R.string.text_product_edit_error
		UpdateError.OTHER -> R.string.text_update_error
	}
}
