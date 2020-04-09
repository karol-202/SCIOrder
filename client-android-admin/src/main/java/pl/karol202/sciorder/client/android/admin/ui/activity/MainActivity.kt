package pl.karol202.sciorder.client.android.admin.ui.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import kotlinx.android.synthetic.main.activity_main.*
import pl.karol202.sciorder.client.android.admin.R

class MainActivity : AppCompatActivity()
{
	private val navController by lazy { findNavController(R.id.fragmentNavHost) }
	private val appBarConfiguration by lazy { AppBarConfiguration(setOf(R.id.loginFragment, R.id.storesFragment)) }
	
	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		
		initToolbar()
	}
	
	private fun initToolbar()
	{
		setSupportActionBar(toolbar)
		// setupWithNavController(Toolbar, NavController) not used in order to make up button calling onOptionsItemSelected
		NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
	}
	
	override fun onOptionsItemSelected(item: MenuItem) = when(item.itemId)
	{
		android.R.id.home -> onBackPressedDispatcher.onBackPressed().let { true }
		else -> super.onOptionsItemSelected(item)
	}
}
