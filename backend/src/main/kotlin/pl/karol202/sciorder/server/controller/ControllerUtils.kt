package pl.karol202.sciorder.server.controller

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.auth.principal
import io.ktor.http.HttpStatusCode
import io.ktor.request.receiveOrNull
import io.ktor.response.header
import io.ktor.response.respond
import io.ktor.util.pipeline.PipelineContext
import io.ktor.util.pipeline.PipelineInterceptor
import pl.karol202.sciorder.server.auth.AbstractPrincipal

typealias RequestHandler = suspend (PipelineInterceptor<Unit, ApplicationCall>) -> Unit
private typealias Context = PipelineContext<Unit, ApplicationCall>

val Context.requestHandler: RequestHandler
	get() = { this.it(Unit) }

suspend fun Context.ok() = call.respond(HttpStatusCode.OK)

suspend fun Context.ok(message: Any) = call.respond(HttpStatusCode.OK, message)

suspend fun Context.created(message: Any) = call.respond(HttpStatusCode.Created, message)

fun Context.setHeader(header: String, value: String) = call.response.header(header, value)

fun Context.requireLongParameter(paramName: String) =
		call.parameters[paramName]?.toLongOrNull() ?: badRequest("Missing numeric parameter: $paramName")

suspend inline fun <reified T : Any> Context.requireBody(validate: T.() -> Boolean = { true }) =
		call.receiveOrNull<T>()?.takeIf(validate) ?: badRequest("Missing or invalid body of type ${T::class.simpleName}")

suspend fun Context.requirePrincipal(validate: suspend (AbstractPrincipal) -> Boolean) =
		call.principal<AbstractPrincipal>()?.also { if(!validate(it)) forbidden() } ?: unauthorized()
