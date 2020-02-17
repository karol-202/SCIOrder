package pl.karol202.sciorder.server.util

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.util.pipeline.PipelineContext

private typealias Context = PipelineContext<*, ApplicationCall>

suspend fun Context.ok() = call.respond(HttpStatusCode.OK)

suspend fun Context.ok(message: Any) = call.respond(HttpStatusCode.OK, message)

suspend fun Context.created(message: Any) = call.respond(HttpStatusCode.Created, message)
