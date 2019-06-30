import org.gradle.api.Project
import org.gradle.api.initialization.Settings
import org.gradle.kotlin.dsl.provideDelegate
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

val Project.booleanProperties: ReadOnlyProperty<Any, Boolean>
	get() = object : ReadOnlyProperty<Any, Boolean> {
		override fun getValue(thisRef: Any, property: KProperty<*>) = (property(property.name) as String).toBoolean()
	}

val Settings.booleanProperties: ReadOnlyProperty<Any, Boolean>
	get() = object : ReadOnlyProperty<Any, Boolean> {
		override fun getValue(thisRef: Any, property: KProperty<*>) =
				this@booleanProperties.provideDelegate(thisRef, property).getValue<String>(thisRef, property).toBoolean()
	}
