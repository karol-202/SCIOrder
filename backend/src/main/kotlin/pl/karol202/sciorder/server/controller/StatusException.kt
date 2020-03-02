package pl.karol202.sciorder.server.controller

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond

sealed class StatusException(message: String? = null) : RuntimeException(message)
{
	class BadRequest(message: String) : StatusException(message)
	{
		override suspend fun handle(requestHandler: RequestHandler) = requestHandler {
			call.respond(HttpStatusCode.BadRequest, message ?: "")
		}
	}
	
	class Unauthorized : StatusException()
	{
		override suspend fun handle(requestHandler: RequestHandler) = requestHandler {
			call.respond(HttpStatusCode.Unauthorized)
		}
	}
	
	class Forbidden : StatusException()
	{
		override suspend fun handle(requestHandler: RequestHandler) = requestHandler {
			call.respond(HttpStatusCode.Forbidden)
		}
	}
	
	class NotFound : StatusException()
	{
		override suspend fun handle(requestHandler: RequestHandler) = requestHandler {
			call.respond(HttpStatusCode.NotFound)
		}
	}
	
	class Conflict : StatusException()
	{
		override suspend fun handle(requestHandler: RequestHandler) = requestHandler {
			call.respond(HttpStatusCode.Conflict)
		}
	}
	
	class InternalError : StatusException()
	{
		override suspend fun handle(requestHandler: RequestHandler) = requestHandler {
			call.respond(HttpStatusCode.InternalServerError)
		}
	}
	
	companion object
	{
		val allExceptions = StatusException::class.sealedSubclasses
	}
	
	abstract suspend fun handle(requestHandler: RequestHandler)
}

fun badRequest(message: String): Nothing = throw StatusException.BadRequest(message)

fun unauthorized(): Nothing = throw StatusException.Unauthorized()

fun forbidden(): Nothing = throw StatusException.Forbidden()

fun notFound(): Nothing = throw StatusException.NotFound()

fun conflict(): Nothing = throw StatusException.Conflict()

fun internalError(): Nothing = throw StatusException.InternalError()
