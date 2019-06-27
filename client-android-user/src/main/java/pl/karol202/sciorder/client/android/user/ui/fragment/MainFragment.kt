package pl.karol202.sciorder.client.android.user.ui.fragment

import pl.karol202.sciorder.client.android.common.ui.fragment.MainFragment
import pl.karol202.sciorder.client.android.common.util.ctx
import pl.karol202.sciorder.client.android.user.ui.adapter.NavigationPagerAdapter

class MainFragment : MainFragment()
{
	override fun createNavigationPagerAdapter() = NavigationPagerAdapter(ctx, childFragmentManager)
}
