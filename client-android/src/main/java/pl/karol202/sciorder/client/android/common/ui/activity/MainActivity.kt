package pl.karol202.sciorder.client.android.common.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*
import pl.karol202.sciorder.client.android.common.R

abstract class MainActivity : AppCompatActivity()
{
	protected val navController by lazy { findNavController(R.id.fragmentNavHost) }
	private val appBarConfiguration by lazy { AppBarConfiguration(topLevelDestinations) }

	abstract val topLevelDestinations: Set<Int>

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		setSupportActionBar(toolbar)
	}

	override fun onResume()
	{
		super.onResume()
		// If toolbar was set up in onCreate, title would be overwritten by Activity's onPostCreate()
		toolbar.setupWithNavController(navController, appBarConfiguration)
	}

	override fun onBackPressed()
	{
		if(!shouldBlockBackButton()) super.onBackPressed()
	}

	abstract fun shouldBlockBackButton(): Boolean
}
