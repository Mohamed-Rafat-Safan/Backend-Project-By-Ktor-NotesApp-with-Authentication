package com.example.routing

import com.example.db.DatabaseConnection
import com.example.entities.NoteEntity
import com.example.models.NoteRequest
import com.example.models.ResponseData
import com.example.models.Notes
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.dsl.*

fun Application.noteRoutes() {
    val db = DatabaseConnection.database
    routing {
        get("/notes") {
            val notes = db.from(NoteEntity).select().map {
                val id = it[NoteEntity.id]
                val title = it[NoteEntity.title]
                val note = it[NoteEntity.note]
                Notes(id ?: -1, title ?: "", note ?: "")
            }

            call.respond(notes)
        }

        get("/notes/{id}") {
            val idSelect = call.parameters["id"]?.toInt() ?: -1
            val note = db.from(NoteEntity).select().where(NoteEntity.id eq idSelect).map {
                val id = it[NoteEntity.id]!!
                val title = it[NoteEntity.title]!!
                val note = it[NoteEntity.note]!!
                Notes(id = id, title = title, note = note)
            }.firstOrNull()

            if (note == null) {
                call.respond(HttpStatusCode.NotFound, ResponseData(data = "Note Not Found", isSuccess = false))
            } else {
                call.respond(HttpStatusCode.OK, ResponseData(data = note, isSuccess = true))
            }
        }

        post("/addNote") {
            val request = call.receive<NoteRequest>()
            val result = db.insert(NoteEntity) {
                set(it.title, request.title)
                set(it.note, request.note)
            }

            if (result == 1) {
                call.respond(
                    HttpStatusCode.OK, ResponseData(
                        data = "The Note Is Inserted Successfully",
                        isSuccess = true
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.BadRequest, ResponseData(
                        data = "Fail In Insert Note",
                        isSuccess = false
                    )
                )
            }
        }

        put("/updateNote/{id}") {
            val selectId = call.parameters["id"]?.toInt() ?: -1

            val request = call.receive<NoteRequest>()

            val noteUpdate = db.update(NoteEntity) {
                set(it.title, request.title)
                set(it.note, request.note)

                where {
                    it.id eq selectId
                }
            }

            if (noteUpdate == 1) {
                call.respond(
                    HttpStatusCode.OK, ResponseData(
                        data = "The Note Is Updated Successfully",
                        isSuccess = true
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.BadRequest, ResponseData(
                        data = "Fail In Update Note",
                        isSuccess = false
                    )
                )
            }

        }

        delete("/deleteNote/{id}") {
            val selectId = call.parameters["id"]?.toInt() ?: -1

            val noteDelete = db.delete(NoteEntity) { it.id eq selectId }

            if (noteDelete == 1) {
                call.respond(
                    HttpStatusCode.OK, ResponseData(
                        data = "The Note Is Deleted Successfully",
                        isSuccess = true
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.BadRequest, ResponseData(
                        data = "Fail In Delete Note",
                        isSuccess = false
                    )
                )
            }

        }

    }
}