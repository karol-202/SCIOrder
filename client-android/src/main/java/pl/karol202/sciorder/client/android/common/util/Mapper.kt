package pl.karol202.sciorder.client.android.common.util

interface ToEntityMapper<E, M>
{
	fun toEntity(model: M): E
}

interface ToModelMapper<E, M>
{
	fun toModel(entity: E): M
}

fun <E, M> E.toModel(mapper: ToModelMapper<E, M>) = mapper.toModel(this)

fun <E, M> Iterable<E>.toModels(mapper: ToModelMapper<E, M>) = map { mapper.toModel(it) }

fun <E, M> M.toEntity(mapper: ToEntityMapper<E, M>) = mapper.toEntity(this)

fun <E, M> Iterable<M>.toEntities(mapper: ToEntityMapper<E, M>) = map { mapper.toEntity(it) }

fun <E, K, V> Map<K, V>.toEntities(mapper: ToEntityMapper<E, Map.Entry<K, V>>) = entries.toEntities(mapper)
