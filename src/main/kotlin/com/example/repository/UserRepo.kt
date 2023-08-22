package com.example.repository

import com.example.db.DatabaseConnection
import com.example.entities.UserEntity
import com.example.models.User
import com.example.models.UserRegisterBody
import org.ktorm.dsl.*

class UserRepo {
    val db = DatabaseConnection.database
    fun getUserByEmail(email: String): User? {
        return db.from(UserEntity).select().where(UserEntity.email eq email).map {
            val id = it[UserEntity.id]!!
            val fullName = it[UserEntity.fullName]!!
            val email = it[UserEntity.email]!!
            val password = it[UserEntity.password]!!
            User(id, fullName, email, password)
        }.firstOrNull()
    }

    fun addUser(userRegisterBody: UserRegisterBody): Int {
        return db.insert(UserEntity) {
            set(it.fullName, userRegisterBody.fullName)
            set(it.email, userRegisterBody.email)
            set(it.password, userRegisterBody.password)
        }
    }

    fun userValid(email: String): String? {
        return db.from(UserEntity).select().where(UserEntity.email eq email).map {
            it[UserEntity.email]
        }.firstOrNull()
    }

}