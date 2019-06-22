package pl.karol202.sciorder.client.android.admin.ui.fragment

import pl.karol202.sciorder.client.android.admin.ui.adapter.NavigationPagerAdapter
import pl.karol202.sciorder.client.android.common.extension.ctx
import pl.karol202.sciorder.client.android.common.ui.fragment.MainFragment

class MainFragment : MainFragment()
{
	override fun createNavigationPagerAdapter() = NavigationPagerAdapter(ctx, childFragmentManager)
}
