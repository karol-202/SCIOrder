package pl.karol202.sciorder.client.android.common.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import kotlinx.android.synthetic.main.fragment_main.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import pl.karol202.sciorder.client.android.common.R
import pl.karol202.sciorder.client.android.common.component.FragmentWithMenu
import pl.karol202.sciorder.client.android.common.util.observe
import pl.karol202.sciorder.client.android.common.viewmodel.OwnerAndroidViewModel
import pl.karol202.sciorder.common.Owner

abstract class MainFragment : FragmentWithMenu()
{
	private val ownerViewModel by sharedViewModel<OwnerAndroidViewModel>()

	override val layoutRes = R.layout.fragment_main
	override val menuRes = R.menu.menu_main

	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		initTabs()

		observeOwner()
	}

	private fun initTabs()
	{
		viewPagerMain.adapter = createNavigationPagerAdapter()
		tabLayoutMain.setupWithViewPager(viewPagerMain)
	}

	abstract fun createNavigationPagerAdapter(): PagerAdapter

	private fun observeOwner() =
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
	}
}
