package pl.karol202.sciorder.ui.adapters

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import pl.karol202.sciorder.R
import pl.karol202.sciorder.ui.fragment.OrderTrackFragment
import pl.karol202.sciorder.ui.fragment.ProductsFragment

class NavigationPagerAdapter(private val context: Context,
                             fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager)
{
	override fun getItem(position: Int) = when(position)
	{
		0 -> ProductsFragment()
		1 -> OrderTrackFragment()
		else -> throw IllegalArgumentException()
	}

	override fun getCount() = 2

	override fun getPageTitle(position: Int) = when(position)
	{
		0 -> context.getString(R.string.fragment_products)
		1 -> context.getString(R.string.fragment_order_track)
		else -> throw IllegalArgumentException()
	}
}
