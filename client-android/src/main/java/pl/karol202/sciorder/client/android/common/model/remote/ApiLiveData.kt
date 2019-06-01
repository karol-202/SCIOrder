package pl.karol202.sciorder.client.android.common.model.remote

import androidx.lifecycle.LiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.atomic.AtomicBoolean

class ApiLiveData<T>(private val call: Call<T>) : LiveData<ApiResponse<T>>(), Callback<T>
{
	private val started = AtomicBoolean(false)

	override fun onActive()
	{
		super.onActive()
		if(started.getAndSet(true)) return // Return if already started
		call.enqueue(this)
	}

	override fun onResponse(call: Call<T>, response: Response<T>)
	{
		postValue(ApiResponse.fromRetrofitResponse(response))
	}

	override fun onFailure(call: Call<T>, throwable: Throwable)
	{
		postValue(ApiResponse.fromThrowable(throwable))
	}
}
