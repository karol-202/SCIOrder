package pl.karol202.sciorder.client.android.common.extensions

import pl.karol202.sciorder.common.model.Owner

fun Owner.Companion.create(name: String, hash: String) = Owner("", name, hash)
