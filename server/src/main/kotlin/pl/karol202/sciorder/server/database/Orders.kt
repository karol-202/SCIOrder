package pl.karol202.sciorder.server.database

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.InsertStatement
import pl.karol202.sciorder.model.Order

private object Orders : Table()
{
    val id = integer("id").autoIncrement().primaryKey()
    val name = varchar("name", 20)

    suspend fun createTable(db: Database) = query(db) { SchemaUtils.create(this) }

    suspend fun insert(db: Database, order: Order) = query(db) { insert { it.fromOrder(order) } }

    suspend fun getOrders(db: Database) = query(db) { selectAll().map { it.toOrder() } }

    private fun InsertStatement<Number>.fromOrder(order: Order)
    {
        this[name] = order.name
    }

    private fun ResultRow.toOrder() = Order(name = this[name])
}

suspend fun AppDatabase.insertOrder(order: Order) = Orders.insert(db, order)

suspend fun AppDatabase.getOrders() = Orders.getOrders(db)
