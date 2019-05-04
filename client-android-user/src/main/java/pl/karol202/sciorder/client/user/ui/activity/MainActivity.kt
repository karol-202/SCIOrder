package pl.karol202.sciorder.client.user.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import pl.karol202.sciorder.client.user.R
import pl.karol202.sciorder.client.user.ui.adapters.NavigationPagerAdapter

class MainActivity : AppCompatActivity()
{
	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		setSupportActionBar(toolbar)

		viewPager.adapter = NavigationPagerAdapter(this, supportFragmentManager)

		tabLayout.setupWithViewPager(viewPager)
	}
}
