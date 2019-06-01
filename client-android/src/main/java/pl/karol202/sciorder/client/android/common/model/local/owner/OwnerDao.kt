package pl.karol202.sciorder.client.android.common.model.local.owner

import androidx.lifecycle.LiveData
import pl.karol202.sciorder.common.model.Owner

interface OwnerDao
{
	suspend fun set(owner: Owner?)

	fun get(): LiveData<Owner?>
}
