package pl.karol202.sciorder.client.android.admin.ui.activity

import pl.karol202.sciorder.client.android.admin.R
import pl.karol202.sciorder.client.common.ui.activity.MainActivity

class MainActivity : MainActivity()
{
	override val topLevelDestinations = setOf(R.id.loginFragment, R.id.mainFragment)

	override fun shouldBlockBackButton() = navController.currentDestination?.id == R.id.mainFragment
}
