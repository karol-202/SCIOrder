package pl.karol202.sciorder.client.admin.ui.fragment

import pl.karol202.sciorder.client.admin.ui.adapter.NavigationPagerAdapter
import pl.karol202.sciorder.client.common.extensions.ctx
import pl.karol202.sciorder.client.common.ui.fragment.MainFragment

class MainFragment : MainFragment()
{
	override fun createNavigationPagerAdapter() = NavigationPagerAdapter(ctx, childFragmentManager)
}
