package com.example.entities

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object NoteEntity : Table<Nothing>("note") {
    val id = int("id").primaryKey()
    val title = varchar("title")
    val note = varchar("note")
}