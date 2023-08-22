package com.example.routing

import com.example.db.DatabaseConnection
import com.example.entities.UserEntity
import com.example.models.*
import com.example.repository.UserRepo
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
    val userRepo = UserRepo()

    routing {
        post("/register") {
            val userRequest = call.receive<UserRegisterBody>()

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

            val userValid = userRepo.userValid(email)
            if (userValid != null) {
                call.respond(HttpStatusCode.BadRequest, ResponseData("Email is already taken", false))
                return@post
            }

            val userInsert = userRepo.addUser(UserRegisterBody(fullName, email, password))
            if (userInsert == 1) {
                val userInserted = userRepo.getUserByEmail(email)
                val userResponse = getUserResponse(userInserted!!)

                call.respond(HttpStatusCode.OK, ResponseData(userResponse, true))
            } else {
                call.respond(HttpStatusCode.BadRequest, ResponseData("User Fail Inserted", false))
            }

        }

        post("/login") {
            val userRequest = call.receive<UserLoginBody>()

            val email = userRequest.email
            val password = userRequest.password

            if (!userRequest.isEmailValid()) {
                call.respond(HttpStatusCode.BadRequest, ResponseData("Email no valid", false))
                return@post
            } else if (!userRequest.isPasswordValid()) {
                call.respond(HttpStatusCode.BadRequest, ResponseData("Password must be more than 6 characters", false))
                return@post
            }

            val userChecked = userRepo.getUserByEmail(email)

            if (userChecked == null) {
                call.respond(HttpStatusCode.Unauthorized, ResponseData("Invalid email or password", false))
                return@post
            }

            val passwordMatch = BCrypt.checkpw(password, userChecked.password)
            if (!passwordMatch) {
                call.respond(HttpStatusCode.Unauthorized, ResponseData("Invalid email or password", false))
                return@post
            }

            val userResponse = getUserResponse(userChecked)


            call.respond(HttpStatusCode.OK, ResponseData(userResponse, true))
        }

        authenticate {
            get("/me") {

                val principle = call.principal<JWTPrincipal>()

                if (principle == null) {
                    return@get
                }
                val email = principle!!.payload.getClaim("email").asString()

                val userChecked = userRepo.getUserByEmail(email)!!

                val userInfo = UserInfo(userChecked.id, userChecked.fullName, userChecked.email)

                call.respond(HttpStatusCode.OK, ResponseData(userInfo, true))
            }
        }

    }
}


fun getUserResponse(user: User): UserAuthResponse {
    val tokenManager = TokenManager(HoconApplicationConfig(ConfigFactory.load()))
    val token = tokenManager.generateJWTToken(user)
    return UserAuthResponse(UserInfo(user.id, user.fullName, user.email), token = token)
}