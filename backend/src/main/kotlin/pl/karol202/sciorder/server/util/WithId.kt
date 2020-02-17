package pl.karol202.sciorder.server.util

import pl.karol202.sciorder.common.model.IdProvider

data class WithId<T>(override val id: Any,
                     val value: T) : IdProvider
