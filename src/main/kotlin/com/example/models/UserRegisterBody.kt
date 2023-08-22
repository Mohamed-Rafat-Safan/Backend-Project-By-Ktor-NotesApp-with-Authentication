package com.example.models

import kotlinx.serialization.Serializable
import org.mindrot.jbcrypt.BCrypt

@Serializable
data class UserRegisterBody(
    val fullName: String,
    val email: String,
    val password: String
) {

    fun EncryptingPassword(): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    fun isEmailValid(): Boolean {
        val pattern = Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})")
        return pattern.matches(email)
    }

    fun isPasswordValid(): Boolean {
        return password.length >= 6
    }
}
