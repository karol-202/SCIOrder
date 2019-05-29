package pl.karol202.sciorder.client.common.model.local.owner

import androidx.room.Entity
import androidx.room.PrimaryKey

// Table stores at most one owner
@Entity(tableName = OwnerEntity.TABLE_NAME)
data class OwnerEntity(val id: String,
                       val name: String,
                       val hash: String)
{
	@PrimaryKey
	var fakeId = FAKE_ID
		set(_) { }

	companion object
	{
		const val TABLE_NAME = "owner"
		private const val FAKE_ID = 0
	}
}
