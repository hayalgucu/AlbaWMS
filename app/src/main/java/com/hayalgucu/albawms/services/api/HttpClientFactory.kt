package com.hayalgucu.albawms.services.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import java.util.concurrent.TimeUnit

class HttpClientFactory {

    val httpClient: HttpClient = HttpClient(OkHttp) {
        engine {
            config {
                hostnameVerifier { _, _ -> true }
                readTimeout(40, TimeUnit.SECONDS)
                writeTimeout(40, TimeUnit.SECONDS)
            }
        }
        defaultRequest {
            contentType(ContentType.Application.Json)
        }

        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
                encodeDefaults = true
            })
        }
    }
}