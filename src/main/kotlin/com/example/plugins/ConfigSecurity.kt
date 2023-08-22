package com.example.plugins

import com.example.models.ResponseData
import com.example.utils.TokenManager
import com.typesafe.config.ConfigFactory
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.config.*
import io.ktor.server.response.*

fun Application.configSecurity() {
    val config = HoconApplicationConfig(ConfigFactory.load())
    val tokenManager = TokenManager(config)

    install(Authentication) {
        jwt {
            verifier(tokenManager.verifyJWTToken())
            realm = config.property("realm").toString()

            validate { jwtCredential: JWTCredential ->
                kotlin.run {
                    if (jwtCredential.payload.getClaim("email").asString().isNotEmpty()) {
                        JWTPrincipal(jwtCredential.payload)
                    } else {
                        null
                    }
                }
            }

            challenge { defaultScheme, realm ->
                call.respond(HttpStatusCode.Unauthorized, ResponseData("Token is not valid or has expired", false))
            }

        }
    }

}