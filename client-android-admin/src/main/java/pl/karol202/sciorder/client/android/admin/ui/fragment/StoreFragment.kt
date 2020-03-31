package pl.karol202.sciorder.client.android.admin.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import kotlinx.android.synthetic.main.fragment_store.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import pl.karol202.sciorder.client.android.admin.R
import pl.karol202.sciorder.client.android.admin.ui.adapter.NavigationPagerAdapter
import pl.karol202.sciorder.client.android.common.component.InflatedFragment
import pl.karol202.sciorder.client.android.common.util.ctx
import pl.karol202.sciorder.client.android.common.viewmodel.AdminStoresAndroidViewModel
import pl.karol202.sciorder.common.model.Store

class StoreFragment : InflatedFragment()
{
	private val storesViewModel by sharedViewModel<AdminStoresAndroidViewModel>()
	
	override val layoutRes = R.layout.fragment_store
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		initTabs()
		
		observeSelectedStore()
	}
	
	private fun initTabs()
	{
		viewPagerStore.adapter = NavigationPagerAdapter(ctx, childFragmentManager)
		tabLayoutStore.setupWithViewPager(viewPagerStore)
	}
	
	private fun observeSelectedStore() = storesViewModel.selectedStoreLiveData.observe(viewLifecycleOwner) {
		if(it == null) navigateBack()
		else updateTitle(it)
	}
	
	private fun navigateBack() = parentFragmentManager.popBackStack()
	
	private fun updateTitle(store: Store)
	{
		(activity as? AppCompatActivity)?.supportActionBar?.title = store.name
	}
}
