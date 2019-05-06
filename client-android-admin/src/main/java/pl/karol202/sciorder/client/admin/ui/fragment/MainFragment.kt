package pl.karol202.sciorder.client.admin.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_main.*
import pl.karol202.sciorder.client.admin.R
import pl.karol202.sciorder.client.admin.ui.adapter.NavigationPagerAdapter
import pl.karol202.sciorder.client.common.extensions.ctx

class MainFragment : Fragment()
{
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
			inflater.inflate(R.layout.fragment_main, container, false)

	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		viewPagerMain.adapter = NavigationPagerAdapter(ctx, childFragmentManager)

		tabLayoutMain.setupWithViewPager(viewPagerMain)
	}
}
