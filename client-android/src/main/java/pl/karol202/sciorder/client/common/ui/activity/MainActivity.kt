package pl.karol202.sciorder.client.common.ui.activity

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.karol202.sciorder.client.common.R
import pl.karol202.sciorder.client.common.extensions.observeNonNull
import pl.karol202.sciorder.client.common.viewmodel.OwnerViewModel
import pl.karol202.sciorder.common.model.Owner

abstract class MainActivity : AppCompatActivity()
{
	private val ownerViewModel by viewModel<OwnerViewModel>()

	protected val navController by lazy { findNavController(R.id.fragmentNavHost) }
	private val appBarConfiguration by lazy { AppBarConfiguration(topLevelDestinations) }

	abstract val topLevelDestinations: Set<Int>

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		initToolbar()
		observeOwner()
	}

	private fun initToolbar()
	{
		setSupportActionBar(toolbar)
		toolbar.setupWithNavController(navController, appBarConfiguration)
	}

	private fun observeOwner() = ownerViewModel.ownerLiveData.observeNonNull(this) { updateTitle(it) }

	private fun updateTitle(owner: Owner)
	{
		Handler().post { toolbar.title = owner.name } // TODO Find better way
	}

	override fun onBackPressed()
	{
		if(!shouldBlockBackButton()) super.onBackPressed()
	}

	abstract fun shouldBlockBackButton(): Boolean
}
