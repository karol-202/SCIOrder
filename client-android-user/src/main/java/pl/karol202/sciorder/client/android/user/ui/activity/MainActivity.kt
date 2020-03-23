package pl.karol202.sciorder.client.android.user.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*
import pl.karol202.sciorder.client.android.user.R

class MainActivity : AppCompatActivity()
{
	private val navController by lazy { findNavController(R.id.fragmentNavHost) }
	private val appBarConfiguration by lazy { AppBarConfiguration(setOf(R.id.loginFragment, R.id.mainFragment)) }
	
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
	
	private fun shouldBlockBackButton() = navController.currentDestination?.id == R.id.mainFragment
}
