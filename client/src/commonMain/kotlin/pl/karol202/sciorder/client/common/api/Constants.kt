package pl.karol202.sciorder.client.common.api

import io.ktor.http.auth.AuthScheme

const val HEADER_AUTHORIZATION = "Authorization"

val AuthScheme.Bearer get() = "Bearer"
