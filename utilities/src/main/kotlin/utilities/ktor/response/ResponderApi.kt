/*
 * Copyright (c) 2022.  sagit117@gmail.com <Pavel Aksenov>
 */

package utilities.ktor.response

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import utilities.extensions.log
import utilities.ktor.plugins.AttributeKeys

class ResponderApi(var callContext: ApplicationCall) {
    /**
     * Позволяет наполнять ответ сервера ОК, дополнительными полями
     *
     * @param data дополнительные поля, которые нужно включить в ответ
     */
    suspend fun responseOK(data: Map<String, Any>? = null) {
        val responseJSON: MutableMap<String, Any> = mutableMapOf(
            "status" to "ok",
            "requestID" to callContext.attributes[AttributeKeys.RESPONSE_ID.value]
        )
        if (data != null) responseJSON.putAll(data)

        callContext.respond(HttpStatusCode.OK, responseJSON)
    }

    /**
     * Позволяет наполнять ответ сервера error, дополнительными полями
     *
     * @param error класс ошибки
     * @param data дополнительные поля, которые нужно включить в ответ
     * @param statusCode код ответа
     */
    suspend fun responseError(
        error: Throwable,
        data: Map<String, Any>? = null,
        statusCode: HttpStatusCode? = null
    ) {
        val requestID = callContext.attributes[AttributeKeys.RESPONSE_ID.value]
        val responseJSON: MutableMap<String, Any> = mutableMapOf(
            "status" to "error",
            "requestID" to requestID,
            "description" to error.localizedMessage,
        )
        if (data != null) responseJSON.putAll(data)

        "Client requestID: $requestID error: ${error.localizedMessage ?: error.message}"
            .log(error::class.java.packageName)
            .error()

        val code: HttpStatusCode = statusCode ?: when(error) {
            is CannotTransformContentToTypeException,
            is BadRequestException,
            is IllegalArgumentException,
            is NullPointerException -> HttpStatusCode.BadRequest

            else -> HttpStatusCode.InternalServerError
        }

        callContext.respond(code, responseJSON)
    }
}