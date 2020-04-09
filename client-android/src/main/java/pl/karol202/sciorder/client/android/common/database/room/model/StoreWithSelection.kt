package pl.karol202.sciorder.client.android.common.database.room.model

import pl.karol202.sciorder.common.model.Store

data class StoreWithSelection(val store: Store,
                              val selected: Boolean)

fun Store.selected(selected: Boolean) = StoreWithSelection(this, selected)
