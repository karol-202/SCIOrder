package pl.karol202.sciorder.client.android.common

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import pl.karol202.sciorder.client.android.common.api.apiModule
import pl.karol202.sciorder.client.android.common.database.databaseModule
import pl.karol202.sciorder.client.android.common.repository.repositoryModule
import pl.karol202.sciorder.client.android.common.viewmodel.viewModelsModule

class SCIOrderApplication : Application()
{
	override fun onCreate()
	{
		super.onCreate()
		startKoin {
			androidContext(this@SCIOrderApplication)
			modules(apiModule() + databaseModule() + repositoryModule() + viewModelsModule())
		}
		
		// Temporary solution
		AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
	}
}
