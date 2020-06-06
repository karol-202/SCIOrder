package pl.karol202.sciorder.client.android.user.ui.fragment

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_main.*
import pl.karol202.sciorder.client.android.common.ui.fragment.FragmentWithMenu
import pl.karol202.sciorder.client.android.common.util.ctx
import pl.karol202.sciorder.client.android.user.R
import pl.karol202.sciorder.client.android.user.ui.adapter.NavigationPagerAdapter

abstract class MainFragment : FragmentWithMenu()
{
	//private val ownerViewModel by sharedViewModel<OwnerAndroidViewModel>()
	
	override val layoutRes = R.layout.fragment_main
	override val menuRes = R.menu.menu_main
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		initTabs()
		
		//observeOwner()
	}
	
	private fun initTabs()
	{
		viewPagerMain.adapter = NavigationPagerAdapter(ctx, childFragmentManager)
		tabLayoutMain.setupWithViewPager(viewPagerMain)
	}
	
	/*private fun observeOwner() =
			ownerViewModel.ownerLiveData.observe(viewLifecycleOwner) {
				if(it == null) navigateBack()
				else updateTitle(it)
			}
	
	private fun navigateBack() = fragmentManager?.popBackStack()
	
	private fun updateTitle(owner: Owner)
	{
		(activity as? AppCompatActivity)?.supportActionBar?.title = owner.name
	}
	
	override fun onMenuItemSelected(itemId: Int) = when(itemId)
	{
		R.id.item_logout -> ownerViewModel.logout()
		else -> null
	}*/
}
