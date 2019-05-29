package pl.karol202.sciorder.client.user.ui.fragment

import pl.karol202.sciorder.client.common.extensions.ctx
import pl.karol202.sciorder.client.common.ui.fragment.MainFragment
import pl.karol202.sciorder.client.user.ui.adapter.NavigationPagerAdapter

class MainFragment : MainFragment()
{
	override fun createNavigationPagerAdapter() = NavigationPagerAdapter(ctx, childFragmentManager)
}
