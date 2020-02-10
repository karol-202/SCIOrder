package pl.karol202.sciorder.client.android.common.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// Table stores at most one owner
@Entity
data class OwnerEntity(val id: String?, // Null id indicates absence of owner
                       val name: String,
                       val hash: String)
{
	// This is the only way to add constant primary key.
	// PK must be constant because at most one owner may be saved in DB.
	@PrimaryKey
	var fakeId = FAKE_ID
		set(_) { }

	companion object
	{
		private const val FAKE_ID = 0
	}
}
