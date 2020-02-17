package pl.karol202.sciorder.server.controller

sealed class StatusException : RuntimeException()
{
	class BadRequest : StatusException()
	
	class Unauthorized : StatusException()
	
	class Forbidden : StatusException()
	
	class NotFound : StatusException()
	
	class Conflict : StatusException()
}
