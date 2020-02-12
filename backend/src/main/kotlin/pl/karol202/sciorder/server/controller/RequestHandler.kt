package pl.karol202.sciorder.server.controller

import io.ktor.application.ApplicationCall
import io.ktor.util.pipeline.PipelineContext
import io.ktor.util.pipeline.PipelineInterceptor

typealias RequestHandler = suspend (PipelineInterceptor<Unit, ApplicationCall>) -> Unit

val PipelineContext<Unit, ApplicationCall>.requestHandler: RequestHandler
	get() = { this.it(Unit) }
