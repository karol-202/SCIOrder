package pl.karol202.sciorder.client.common.model.local.owner

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Flowable

@Dao
interface OwnerEntityDao
{
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun set(owner: OwnerEntity)

	@Query("DELETE FROM ${OwnerEntity.TABLE_NAME}")
	suspend fun delete()

	@Query("SELECT * FROM ${OwnerEntity.TABLE_NAME}")
	fun get(): Flowable<OwnerEntity?>
}
