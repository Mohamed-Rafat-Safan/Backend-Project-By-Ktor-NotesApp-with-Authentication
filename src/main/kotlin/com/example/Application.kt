package com.example

import com.example.models.ResponseData
import com.example.plugins.*
import com.example.routing.noteRoutes
import com.example.utils.TokenManager
import com.typesafe.config.ConfigFactory
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.config.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import kotlin.jvm.internal.Intrinsics.Kotlin


fun main() {
    embeddedServer(Netty, port = 8080, host = "127.0.0.1", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configSecurity()

    // this to convert json to object
    // هنا علشان تعرفخ ان فيه json جيلك , ديه بقا علشان تتعرف عليه
    install(ContentNegotiation) {
        json()
    }

    configureRouting()
}



