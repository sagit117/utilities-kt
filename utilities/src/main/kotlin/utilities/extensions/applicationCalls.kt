/*
 * Copyright (c) 2022.  sagit117@gmail.com <Pavel Aksenov>
 */

package utilities.extensions

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*

/** Возвращает или параметры формы, или null */
suspend fun ApplicationCall.receiveParametersOrNull(): Parameters? {
    return try {
        receiveParameters()
    } catch (error: Throwable) {
        return null
    }
}