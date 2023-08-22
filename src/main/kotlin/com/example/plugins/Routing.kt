package com.example.plugins

import com.example.routing.authRoutes
import com.example.routing.noteRoutes
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import java.io.File
import javax.xml.stream.events.StartDocument

fun Application.configureRouting() {

    noteRoutes()
    authRoutes()

    routing {
        get("/") {
//            println("URI: ${call.request.uri}")
//            println("Header: ${call.request.headers.names()}")
//            println("Postman-Token: ${call.request.headers["Postman-Token"]}")
//            println("User-Agent: ${call.request.headers["User-Agent"]}")
//
//            println("params: ${call.request.queryParameters.names()}")
//            println("params Name: ${call.request.queryParameters["name"]}")
//            println("params Age: ${call.request.queryParameters["age"]}")

//            call.respondText("Hello World!")


            // if you get status code
            call.respondText("Hello Mohamed!", status = HttpStatusCode.OK)
        }




//        get("/headers") {
//            call.response.headers.append("server-nane", "shopping")
//            call.response.headers.append("My-Name", "Mohamed")
//            call.respondText("Success")
//        }

//        // download file
//        get("/downloadFile") {
//            val file = File("files/img.jpg")
//
//            call.response.header(
//                HttpHeaders.ContentDisposition,
//                ContentDisposition.Attachment.withParameter(
//                    ContentDisposition.Parameters.FileName, "downloadableImage.jpg"
//                ).toString()
//            )
//
//            call.respondFile(file)
//        }

//        // open file in browser
//        get("/openFile") {
//            val file = File("files/img.jpg")
//
//            call.response.header(
//                HttpHeaders.ContentDisposition,
//                ContentDisposition.Inline.withParameter(
//                    ContentDisposition.Parameters.FileName, "openImage.jpg"
//                ).toString()
//            )
//
//            call.respondFile(file)
//        }


    }
}
