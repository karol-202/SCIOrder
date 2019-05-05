package pl.karol202.sciorder.client.admin.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import pl.karol202.sciorder.client.admin.R
import pl.karol202.sciorder.client.admin.ui.adapter.NavigationPagerAdapter

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
