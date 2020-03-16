package pl.karol202.sciorder.client.android.common.util

interface EntityModelMapper<E : Any, M : Any>
{
	fun toModel(entity: E): M
	
	fun toEntity(model: M): E
}

fun <E : Any, M : Any> E.toModel(mapper: EntityModelMapper<E, M>) = mapper.toModel(this)

fun <E : Any, M : Any> M.toEntity(mapper: EntityModelMapper<E, M>) = mapper.toEntity(this)

fun <E : Any, M : Any> Iterable<E>.toModel(mapper: EntityModelMapper<E, M>) = map { mapper.toModel(it) }

fun <E : Any, M : Any> Iterable<M>.toEntity(mapper: EntityModelMapper<E, M>) = map { mapper.toEntity(it) }
