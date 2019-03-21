package pl.karol202.sciorder.server

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.gson.gson
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import pl.karol202.sciorder.model.Order
import pl.karol202.sciorder.server.database.AppDatabase
import pl.karol202.sciorder.server.database.getOrders
import pl.karol202.sciorder.server.database.insertOrder

fun Application.main()
{
    install(DefaultHeaders)
    install(CallLogging)
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }

    routing {
        get("/getOrders") {
            call.respond(AppDatabase.getOrders())
        }
        post("/addOrder") {
            val order = call.receive<Order>()
            AppDatabase.insertOrder(order)
            call.respond(HttpStatusCode.Created)
        }
    }
}
