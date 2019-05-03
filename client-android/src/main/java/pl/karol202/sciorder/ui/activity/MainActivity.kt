package pl.karol202.sciorder.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*
import pl.karol202.sciorder.R
import pl.karol202.sciorder.ui.adapters.NavigationPagerAdapter

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
