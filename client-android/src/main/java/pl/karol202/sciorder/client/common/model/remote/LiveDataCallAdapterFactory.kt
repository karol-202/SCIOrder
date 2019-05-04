package pl.karol202.sciorder.client.common.model.remote

import androidx.lifecycle.LiveData
import retrofit2.CallAdapter
import retrofit2.CallAdapter.Factory
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class LiveDataCallAdapterFactory : Factory()
{
	override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *>?
	{
		if(getRawType(returnType) != LiveData::class.java) return null

		val liveDataType = getParameterUpperBound(0, returnType as ParameterizedType)
		if(getRawType(liveDataType) != ApiResponse::class.java) return null

		val apiResponseType = getParameterUpperBound(0, liveDataType as ParameterizedType)
		return LiveDataCallAdapter<Any>(apiResponseType)
	}
}
