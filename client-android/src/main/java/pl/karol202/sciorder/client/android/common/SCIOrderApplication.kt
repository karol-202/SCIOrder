package pl.karol202.sciorder.client.android.common

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module

abstract class SCIOrderApplication : Application()
{
	abstract val modules: List<Module>

	override fun onCreate()
	{
		super.onCreate()
		startKoin {
			androidContext(this@SCIOrderApplication)
			modules(modules)
		}
		KoinCommon.loadModules()
	}
}
