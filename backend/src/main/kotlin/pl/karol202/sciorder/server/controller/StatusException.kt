package pl.karol202.sciorder.server.controller

import io.ktor.http.HttpStatusCode
import kotlin.reflect.full.companionObjectInstance

sealed class StatusException : RuntimeException()
{
	abstract class StatusCodeProvider(val statusCode: HttpStatusCode)
	
	class BadRequest : StatusException()
	{
		companion object : StatusCodeProvider(HttpStatusCode.BadRequest)
	}
	
	class Unauthorized : StatusException()
	{
		companion object : StatusCodeProvider(HttpStatusCode.Unauthorized)
	}
	
	class Forbidden : StatusException()
	{
		companion object : StatusCodeProvider(HttpStatusCode.Forbidden)
	}
	
	class NotFound : StatusException()
	{
		companion object : StatusCodeProvider(HttpStatusCode.NotFound)
	}
	
	class Conflict : StatusException()
	{
		companion object : StatusCodeProvider(HttpStatusCode.Conflict)
	}
	
	class InternalError : StatusException()
	{
		companion object : StatusCodeProvider(HttpStatusCode.InternalServerError)
	}
	
	companion object
	{
		val allExceptions = StatusException::class.sealedSubclasses
				.associateWith { (it.companionObjectInstance as StatusCodeProvider).statusCode }
	}
}

fun badRequest(): Nothing = throw StatusException.BadRequest()

fun unauthorized(): Nothing = throw StatusException.Unauthorized()

fun forbidden(): Nothing = throw StatusException.Forbidden()

fun notFound(): Nothing = throw StatusException.NotFound()

fun conflict(): Nothing = throw StatusException.Conflict()

fun internalError(): Nothing = throw StatusException.InternalError()
