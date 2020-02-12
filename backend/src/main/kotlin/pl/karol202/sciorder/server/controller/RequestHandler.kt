package pl.karol202.sciorder.server.controller

import io.ktor.application.ApplicationCall
import io.ktor.util.pipeline.PipelineInterceptor

typealias RequestHandler = (PipelineInterceptor<Unit, ApplicationCall>) -> Unit
