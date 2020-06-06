package pl.karol202.sciorder.client.android.admin.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.lifecycle.observe
import androidx.navigation.fragment.NavHostFragment
import kotlinx.android.synthetic.main.fragment_store.*
import kotlinx.android.synthetic.main.fragment_store.appBarLayout
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import pl.karol202.sciorder.client.android.admin.R
import pl.karol202.sciorder.client.android.admin.ui.activity.ToolbarActivity
import pl.karol202.sciorder.client.android.admin.ui.adapter.NavigationPagerAdapter
import pl.karol202.sciorder.client.android.common.ui.fragment.InflatedFragment
import pl.karol202.sciorder.client.android.common.util.ctx
import pl.karol202.sciorder.client.android.common.util.inflateTransition
import pl.karol202.sciorder.client.android.common.util.observeNonNull
import pl.karol202.sciorder.client.android.common.util.sharedElements
import pl.karol202.sciorder.client.android.common.viewmodel.AdminProductEditAndroidViewModel
import pl.karol202.sciorder.client.android.common.viewmodel.AdminStoresAndroidViewModel
import pl.karol202.sciorder.common.model.Store

class StoreFragment : InflatedFragment()
{
	private val storesViewModel by sharedViewModel<AdminStoresAndroidViewModel>()
	private val productEditViewModel by sharedViewModel<AdminProductEditAndroidViewModel>()
	
	private val navController by lazy { NavHostFragment.findNavController(this) }
	
	override val layoutRes = R.layout.fragment_store
	
	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		sharedElementEnterTransition = ctx.inflateTransition(android.R.transition.move)
		//enterTransition = ctx.inflateTransition(android.R.transition.fade)
		exitTransition = ctx.inflateTransition(android.R.transition.fade)
		returnTransition = ctx.inflateTransition(android.R.transition.fade)
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		handleBackPress()
		
		initToolbar()
		initTabs()
		
		observeSelectedStore()
		observeEditedProduct()
	}
	
	private fun handleBackPress() = requireActivity().onBackPressedDispatcher.addCallback(this) {
		storesViewModel.selectStore(null)
	}
	
	private fun initToolbar() = (activity as? ToolbarActivity)?.setToolbar(toolbar)
	
	private fun initTabs()
	{
		viewPagerStore.adapter = NavigationPagerAdapter(ctx, childFragmentManager)
		tabLayoutStore.setupWithViewPager(viewPagerStore)
	}
	
	private fun observeSelectedStore() = storesViewModel.selectedStoreLiveData.observe(viewLifecycleOwner) {
		if(it == null) navigateBack()
		else updateTitle(it)
	}
	
	private fun observeEditedProduct() = productEditViewModel.editedProductLiveData.observeNonNull(viewLifecycleOwner) {
		navigateToProductEditFragment()
	}
	
	private fun navigateBack() = navController.popBackStack()
	
	private fun updateTitle(store: Store)
	{
		toolbar.title = getString(R.string.fragment_store, store.name)
	}
	
	private fun navigateToProductEditFragment() =
			navController.navigate(StoreFragmentDirections.actionStoreToProductEdit(), sharedElements(appBarLayout))
}
