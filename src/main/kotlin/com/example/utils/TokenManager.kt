package com.example.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.example.models.User
import io.ktor.server.config.*
import java.util.*

class TokenManager(val config: HoconApplicationConfig) {
    val secret = config.property("secret").getString()
    val issuer = config.property("issuer").getString()
    val audience = config.property("audience").getString()


    fun generateJWTToken(user: User): String = JWT.create()
        .withAudience(audience)
        .withIssuer(issuer)
        .withClaim("userId", user.id)
        .withClaim("name", user.fullName)
        .withClaim("email", user.email)
        .withExpiresAt(Date(System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000)))
        .sign(Algorithm.HMAC256(secret))


    fun verifyJWTToken(): JWTVerifier {
        return JWT.require(Algorithm.HMAC256(secret))
            .withAudience(audience)
            .withIssuer(issuer)
            .build()
    }

}