package pl.karol202.sciorder.client.android.admin.ui.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.android.synthetic.main.activity_main.*
import pl.karol202.sciorder.client.android.admin.R

class MainActivity : AppCompatActivity(), ToolbarActivity
{
	private val navController by lazy { findNavController(R.id.fragmentNavHost) }
	private val appBarConfiguration by lazy { AppBarConfiguration(setOf(R.id.loginFragment, R.id.storesFragment)) }
	
	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
	}
	
	override fun onOptionsItemSelected(item: MenuItem) = when(item.itemId)
	{
		android.R.id.home -> onBackPressedDispatcher.onBackPressed().let { true }
		else -> super.onOptionsItemSelected(item)
	}
	
	override fun setToolbar(toolbar: Toolbar)
	{
		setSupportActionBar(toolbar)
		// setupWithNavController(Toolbar, NavController) not used in order to make up button calling onOptionsItemSelected
		NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
	}
}
