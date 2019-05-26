package pl.karol202.sciorder.client.common.settings

import androidx.lifecycle.MutableLiveData

fun Settings.liveString(key: String, defaultValue: String?): MutableLiveData<String?> =
		SettingsLiveData(string(key, defaultValue))

fun Settings.liveBoolean(key: String, defaultValue: Boolean): MutableLiveData<Boolean> =
		SettingsLiveData(boolean(key, defaultValue))

fun Settings.liveFloat(key: String, defaultValue: Float): MutableLiveData<Float> =
		SettingsLiveData(float(key, defaultValue))

fun Settings.liveInt(key: String, defaultValue: Int): MutableLiveData<Int> =
		SettingsLiveData(int(key, defaultValue))

fun Settings.liveLong(key: String, defaultValue: Long): MutableLiveData<Long> =
		SettingsLiveData(long(key, defaultValue))

class SettingsLiveData<T>(private val setting: Settings.Entry<T>) : MutableLiveData<T>()
{
	private val listener = { updateInternalValue() }

	private fun updateInternalValue()
	{
		//Change value of LiveData without calling Settings.Entry.set()
		super.setValue(setting.get())
	}

	override fun onActive()
	{
		super.onActive()
		updateInternalValue()
		setting.registerUpdateListener(listener)
	}

	override fun onInactive()
	{
		super.onInactive()
		setting.unregisterUpdateListener(listener)
	}

	override fun getValue(): T?
	{
		updateInternalValue()
		return super.getValue()
	}

	override fun setValue(value: T)
	{
		setting.set(value)
		super.setValue(value)
	}
}
