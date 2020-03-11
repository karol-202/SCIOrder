package pl.karol202.sciorder.client.common.api

import io.ktor.http.auth.AuthScheme

const val HEADER_AUTHENTICATION = "Authentication"

val AuthScheme.Bearer get() = "Bearer"
