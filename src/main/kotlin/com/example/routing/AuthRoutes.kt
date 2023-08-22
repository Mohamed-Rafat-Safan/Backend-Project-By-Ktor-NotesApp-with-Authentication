package com.example.routing

import com.example.db.DatabaseConnection
import com.example.entities.UserEntity
import com.example.models.ResponseData
import com.example.models.User
import com.example.models.UserLogin
import com.example.models.UserRegister
import com.example.utils.TokenManager
import com.typesafe.config.ConfigFactory
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.config.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.dsl.*
import org.mindrot.jbcrypt.BCrypt


fun Application.authRoutes() {
    val db = DatabaseConnection.database
    val tokenManager = TokenManager(HoconApplicationConfig(ConfigFactory.load()))
    routing {
        post("/register") {
            val userRequest = call.receive<UserRegister>()

            val fullName = userRequest.fullName
            val email = userRequest.email
            val password = userRequest.EncryptingPassword()

            if (!userRequest.isEmailValid()) {
                call.respond(HttpStatusCode.BadRequest, ResponseData("Email no valid", false))
                return@post
            } else if (!userRequest.isPasswordValid()) {
                call.respond(HttpStatusCode.BadRequest, ResponseData("Password must be more than 6 characters", false))
                return@post
            }

            val userValid = db.from(UserEntity).select().where(UserEntity.email eq email).map {
                it[UserEntity.email]
            }.firstOrNull()

            if (userValid != null) {
                call.respond(HttpStatusCode.BadRequest, ResponseData("Email is already taken", false))
                return@post
            }

            val userInsert = db.insert(UserEntity) {
                set(it.fullName, fullName)
                set(it.email, email)
                set(it.password, password)
            }

            if (userInsert == 1) {
                call.respond(HttpStatusCode.OK, ResponseData("User Is Inserted Successfully", true))
            } else {
                call.respond(HttpStatusCode.BadRequest, ResponseData("User Fail Inserted", false))
            }

        }

        post("/login") {
            val userRequest = call.receive<UserLogin>()

            val email = userRequest.email
            val password = userRequest.password

            if (!userRequest.isEmailValid()) {
                call.respond(HttpStatusCode.BadRequest, ResponseData("Email no valid", false))
                return@post
            } else if (!userRequest.isPasswordValid()) {
                call.respond(HttpStatusCode.BadRequest, ResponseData("Password must be more than 6 characters", false))
                return@post
            }

            val userChecked = db.from(UserEntity).select().where(UserEntity.email eq email).map {
                val id = it[UserEntity.id]!!
                val fullName = it[UserEntity.fullName]!!
                val email = it[UserEntity.email]!!
                val password = it[UserEntity.password]!!
                User(id, fullName, email, password)
            }.firstOrNull()

            if (userChecked == null) {
                call.respond(HttpStatusCode.Unauthorized, ResponseData("Invalid email or password", false))
                return@post
            }

            val passwordMatch = BCrypt.checkpw(password, userChecked.password)
            if (!passwordMatch) {
                call.respond(HttpStatusCode.Unauthorized, ResponseData("Invalid email or password", false))
                return@post
            }

            val token = tokenManager.generateJWTToken(userChecked)


            call.respond(HttpStatusCode.OK, ResponseData(token, true))

        }

        authenticate {
            get("/me") {

                val principle = call.principal<JWTPrincipal>()

                if (principle == null) {
                    call.respond(HttpStatusCode.NotFound, ResponseData("The Token has expired", false))
                    return@get
                }
                val email = principle!!.payload.getClaim("email").asString()
                val userId = principle!!.payload.getClaim("userId").asInt()

                call.respondText("Hello, $email with id: $userId")
            }
        }

    }
}


//fun createTableUser(){
//    val db = DatabaseConnection.database
//
//    db.from().database.ex
//}