package pl.karol202.sciorder.client.android.common.repository.resource

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pl.karol202.sciorder.client.android.common.extensions.observeOnceNonNull
import pl.karol202.sciorder.common.model.IdProvider

abstract class RoomResource<T : IdProvider> @MainThread constructor(private val coroutineScope: CoroutineScope,
                                                                    private val dao: CrudDao<T>,
                                                                    databaseSource: LiveData<List<T>> = dao.getAll()) :
		MixedResource<List<T>>(databaseSource)
{
	override fun saveToDatabase(data: List<T>)
	{
		databaseSource.observeOnceNonNull { oldData ->
			coroutineScope.launch { dao.dispatchDiff(oldData, data) }
		}
	}
}
