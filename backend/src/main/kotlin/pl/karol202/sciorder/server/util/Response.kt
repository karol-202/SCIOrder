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

suspend fun Context.badRequest() = call.respond(HttpStatusCode.BadRequest)

suspend fun Context.unauthorized() = call.respond(HttpStatusCode.Unauthorized)

suspend fun Context.forbidden() = call.respond(HttpStatusCode.Forbidden)

suspend fun Context.notFound() = call.respond(HttpStatusCode.NotFound)

suspend fun Context.conflict() = call.respond(HttpStatusCode.Conflict)
