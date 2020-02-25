package pl.karol202.sciorder.server.controller

sealed class StatusException : RuntimeException()
{
	class BadRequest : StatusException()
	
	class Unauthorized : StatusException()
	
	class Forbidden : StatusException()
	
	class NotFound : StatusException()
	
	class Conflict : StatusException()
	
	class InternalError : StatusException()
}

fun badRequest(): Nothing = throw StatusException.BadRequest()

fun unauthorized(): Nothing = throw StatusException.Unauthorized()

fun forbidden(): Nothing = throw StatusException.Forbidden()

fun notFound(): Nothing = throw StatusException.NotFound()

fun conflict(): Nothing = throw StatusException.Conflict()

fun internalError(): Nothing = throw StatusException.InternalError()
