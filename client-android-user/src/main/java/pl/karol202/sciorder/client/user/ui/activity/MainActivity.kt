package pl.karol202.sciorder.client.user.ui.activity

import pl.karol202.sciorder.client.common.ui.activity.MainActivity
import pl.karol202.sciorder.client.user.R

class MainActivity : MainActivity()
{
	override val topLevelDestinations = setOf(R.id.loginFragment, R.id.mainFragment)

	override fun shouldBlockBackButton() = navController.currentDestination?.id == R.id.mainFragment
}
