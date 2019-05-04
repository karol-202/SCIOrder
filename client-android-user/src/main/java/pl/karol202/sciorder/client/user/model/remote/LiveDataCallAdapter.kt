package pl.karol202.sciorder.client.user.model.remote

import androidx.lifecycle.LiveData
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

class LiveDataCallAdapter<R>(private val responseType: Type) : CallAdapter<R, LiveData<ApiResponse<R>>>
{
	override fun adapt(call: Call<R>) = ApiLiveData(call)

	override fun responseType() = responseType
}
