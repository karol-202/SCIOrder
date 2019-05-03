package pl.karol202.sciorder.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*
import pl.karol202.sciorder.R

class MainActivity : AppCompatActivity()
{
	private val navController by lazy { findNavController(R.id.fragmentNavHost) }
	private val appBarConfiguration by lazy { AppBarConfiguration(navController.graph) }

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		initToolbar()
	}

	private fun initToolbar()
	{
		toolbar.setupWithNavController(navController, appBarConfiguration)
	}
}
