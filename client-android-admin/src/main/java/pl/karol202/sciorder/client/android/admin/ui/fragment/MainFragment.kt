package pl.karol202.sciorder.client.android.admin.ui.fragment

import pl.karol202.sciorder.client.android.admin.ui.adapter.NavigationPagerAdapter
import pl.karol202.sciorder.client.android.common.ui.fragment.MainFragment
import pl.karol202.sciorder.client.android.common.util.ctx

class MainFragment : MainFragment()
{
	override fun createNavigationPagerAdapter() = NavigationPagerAdapter(ctx, childFragmentManager)
}
