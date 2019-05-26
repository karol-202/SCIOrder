package pl.karol202.sciorder.client.common.settings

import android.content.Context

interface Settings
{
	companion object
	{
		fun createPreferencesSettings(context: Context) = PreferencesSettings(context)
	}

	interface Entry<T>
	{
		fun get(): T

		fun set(value: T)

		fun registerUpdateListener(listener: () -> Unit)

		fun unregisterUpdateListener(listener: () -> Unit)
	}

	fun string(key: String, defaultValue: String?): Entry<String?>

	fun boolean(key: String, defaultValue: Boolean): Entry<Boolean>

	fun float(key: String, defaultValue: Float): Entry<Float>

	fun int(key: String, defaultValue: Int): Entry<Int>

	fun long(key: String, defaultValue: Long): Entry<Long>
}
