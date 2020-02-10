package pl.karol202.sciorder.client.android.common.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Flowable
import pl.karol202.sciorder.client.android.common.database.entity.OwnerEntity

@Dao
interface OwnerEntityDao
{
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun set(owner: OwnerEntity)

	@Query("SELECT * FROM ${OwnerEntity.TABLE_NAME}")
	fun get(): Flowable<OwnerEntity>
}
