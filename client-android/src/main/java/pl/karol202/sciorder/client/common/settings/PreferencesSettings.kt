package pl.karol202.sciorder.client.common.settings

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager

class PreferencesSettings(context: Context) : Settings
{
	private inner class Entry<T>(val key: String,
	                             private val getFunction: () -> T,
	                             private val setFunction: (T) -> Unit) : Settings.Entry<T>
	{
		private val listeners = mutableMapOf<() -> Unit, SharedPreferences.OnSharedPreferenceChangeListener>()

		override fun get() = getFunction()

		override fun set(value: T) = setFunction(value)

		override fun registerUpdateListener(listener: () -> Unit)
		{
			val preferenceListener = SharedPreferences.OnSharedPreferenceChangeListener { _, _key ->
				if(_key == key) listener()
			}
			listeners[listener] = preferenceListener
			sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceListener)
		}

		override fun unregisterUpdateListener(listener: () -> Unit)
		{
			val preferenceListener = listeners[listener] ?: return
			listeners.remove(listener)
			sharedPreferences.unregisterOnSharedPreferenceChangeListener(preferenceListener)
		}
	}

	private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

	override fun string(key: String, defaultValue: String?): Settings.Entry<String?> =
			Entry(key,
			      { sharedPreferences.getString(key, defaultValue) },
			      { value -> sharedPreferences.edit { putString(key, value) } })

	override fun boolean(key: String, defaultValue: Boolean): Settings.Entry<Boolean> =
			Entry(key,
			      { sharedPreferences.getBoolean(key, defaultValue) },
			      { value -> sharedPreferences.edit { putBoolean(key, value) } })

	override fun float(key: String, defaultValue: Float): Settings.Entry<Float> =
			Entry(key,
			      { sharedPreferences.getFloat(key, defaultValue) },
			      { value -> sharedPreferences.edit { putFloat(key, value) } })

	override fun int(key: String, defaultValue: Int): Settings.Entry<Int> =
			Entry(key,
			      { sharedPreferences.getInt(key, defaultValue) },
			      { value -> sharedPreferences.edit { putInt(key, value) } })

	override fun long(key: String, defaultValue: Long): Settings.Entry<Long> =
			Entry(key,
			      { sharedPreferences.getLong(key, defaultValue) },
			      { value -> sharedPreferences.edit { putLong(key, value) } })
}
