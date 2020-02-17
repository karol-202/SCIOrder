package pl.karol202.sciorder.server.entity.mapping

import org.jetbrains.exposed.dao.Entity
import pl.karol202.sciorder.common.model.IdProvider
import pl.karol202.sciorder.common.model.ids

interface Updatable<M : Any>
{
	fun update(model: M)
}

interface OrderedUpdatable<M : Any> : Updatable<M>
{
	var ordinal: Int
}

fun <E, M> Iterable<E>.dispatchTo(targetModels: Iterable<M>, entityProducer: () -> E)
		where E : Entity<*>, E : Updatable<M>,
		      M : IdProvider =
		(this.ids + targetModels.ids).forEach { id ->
			val currentEntity = this.singleOrNull { it.id == id }
			val targetModel = targetModels.singleOrNull { it.id == id }
			val targetIndex = targetModels.indexOf(targetModel)
			
			val newEntity = when
			{
				currentEntity == null && targetModel != null -> entityProducer().also { it.update(targetModel) }
				currentEntity != null && targetModel != null -> currentEntity.also { it.update(targetModel) }
				currentEntity != null && targetModel == null -> currentEntity.also { it.delete() }
				else -> error("Should never happen (maybe ids aren't distinct?)")
			}
			if(newEntity is OrderedUpdatable<*>) newEntity.ordinal = targetIndex
		}

private val Iterable<Entity<*>>.ids get() = map { it.id.value }
