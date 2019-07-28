package pl.karol202.sciorder.client.common.model

import pl.karol202.sciorder.common.Owner

fun Owner.Companion.create(name: String, hash: String) = Owner("", name, hash)
