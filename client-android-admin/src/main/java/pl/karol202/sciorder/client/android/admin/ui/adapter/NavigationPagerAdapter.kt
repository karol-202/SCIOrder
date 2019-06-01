package pl.karol202.sciorder.client.android.admin.ui.adapter

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import pl.karol202.sciorder.client.android.admin.R
import pl.karol202.sciorder.client.android.admin.ui.fragment.OrdersFragment
import pl.karol202.sciorder.client.android.admin.ui.fragment.ProductsFragment

class NavigationPagerAdapter(private val context: Context,
                             fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
{
	override fun getItem(position: Int) = when(position)
	{
		0 -> OrdersFragment()
		1 -> ProductsFragment()
		else -> throw IllegalArgumentException()
	}

	override fun getCount() = 2

	override fun getPageTitle(position: Int): String = when(position)
	{
		0 -> context.getString(R.string.fragment_orders)
		1 -> context.getString(R.string.fragment_products)
		else -> throw IllegalArgumentException()
	}
}
