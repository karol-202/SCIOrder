package pl.karol202.sciorder.client.android.admin.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.lifecycle.observe
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_stores.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import pl.karol202.sciorder.client.android.admin.R
import pl.karol202.sciorder.client.android.admin.ui.adapter.StoreAdapter
import pl.karol202.sciorder.client.android.common.component.FragmentWithMenu
import pl.karol202.sciorder.client.android.common.util.ctx
import pl.karol202.sciorder.client.android.common.util.observeEvent
import pl.karol202.sciorder.client.android.common.util.observeNonNull
import pl.karol202.sciorder.client.android.common.util.showSnackbar
import pl.karol202.sciorder.client.android.common.viewmodel.AdminLoginAndroidViewModel
import pl.karol202.sciorder.client.android.common.viewmodel.AdminStoresAndroidViewModel

class StoresFragment : FragmentWithMenu()
{
	private val loginViewModel by sharedViewModel<AdminLoginAndroidViewModel>()
	private val storesViewModel by sharedViewModel<AdminStoresAndroidViewModel>()
	
	private val navController by lazy { NavHostFragment.findNavController(this) }
	
	private val adapter = StoreAdapter { storesViewModel.selectStore(it) }
	
	override val layoutRes = R.layout.fragment_stores
	override val menuRes = R.menu.menu_stores
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		handleBackPress()
		
		initRefreshLayout()
		initRecycler()
		
		observeAdmin()
		observeStores()
		observeLoading()
		observeLoadingError()
		observeUpdateError()
		observeSelectedStore()
	}
	
	private fun handleBackPress() = requireActivity().onBackPressedDispatcher.addCallback(this) {
		requireActivity().finish()
	}
	
	private fun initRefreshLayout() = refreshLayoutStores.setOnRefreshListener { storesViewModel.refreshStores() }
	
	private fun initRecycler()
	{
		recyclerStores.layoutManager = LinearLayoutManager(ctx)
		recyclerStores.adapter = adapter
	}
	
	private fun observeAdmin() = loginViewModel.adminLiveData.observe(viewLifecycleOwner) {
		if(it == null) navigateBack()
	}
	
	private fun observeStores() = storesViewModel.storesLiveData.observe(viewLifecycleOwner) {
		adapter.stores = it
	}
	
	private fun observeLoading() = storesViewModel.loadingLiveData.observe(viewLifecycleOwner) {
		if(!it) refreshLayoutStores.isRefreshing = false
	}
	
	private fun observeLoadingError() = storesViewModel.loadingErrorEventLiveData.observeEvent(viewLifecycleOwner) {
		showSnackbar(R.string.text_loading_error)
	}
	
	private fun observeUpdateError() = storesViewModel.updateEventLiveData.observeEvent(viewLifecycleOwner) {
		showSnackbar(R.string.text_update_error)
	}
	
	private fun observeSelectedStore() = storesViewModel.selectedStoreLiveData.observeNonNull(viewLifecycleOwner) {
		goToStoreFragment()
	}
	
	private fun navigateBack() = navController.popBackStack()
	
	private fun goToStoreFragment() = navController.navigate(StoresFragmentDirections.actionStoresToStore())
	
	override fun onMenuItemSelected(itemId: Int) = when(itemId)
	{
		R.id.item_logout -> loginViewModel.logout()
		else -> null
	}
}
