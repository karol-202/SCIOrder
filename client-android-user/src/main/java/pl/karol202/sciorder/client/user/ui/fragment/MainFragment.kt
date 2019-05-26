package pl.karol202.sciorder.client.user.ui.fragment

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_main.*
import pl.karol202.sciorder.client.common.components.InflatedFragment
import pl.karol202.sciorder.client.common.extensions.ctx
import pl.karol202.sciorder.client.user.R
import pl.karol202.sciorder.client.user.ui.adapter.NavigationPagerAdapter

class MainFragment : InflatedFragment()
{
	override val layoutRes = R.layout.fragment_main

	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		viewPagerMain.adapter = NavigationPagerAdapter(ctx, childFragmentManager)

		tabLayoutMain.setupWithViewPager(viewPagerMain)
	}
}
