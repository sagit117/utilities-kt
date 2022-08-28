/*
 * Copyright (c) 2022.  sagit117@gmail.com <Pavel Aksenov>
 */

package utilities.ktor.plugins

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.sessions.*
import utilities.UtilitiesLibrary
import utilities.extensions.log

/**
 * Плагин формирует ID сессии и сохраняет значение в аттрибуты вызова.
 * Можно запросить call.attributes[AttributeKeys.SESSION_ID.value]
 */
val SessionIdPlugin = createApplicationPlugin(name = "SessionIdPlugin") {
    onCall { call ->
        val session = call.sessions.get<UserSession>()
        val id = session?.sessionID

        /** Установка ID сессии */
        if (id == null) {
            call.sessions.set(UserSession().also {
                "New set session id: ${it.sessionID}".log(this::class.java.name).debug()
                call.attributes.put(AttributeKeys.SESSION_ID.value, it.sessionID)
            })
        } else {
            if (!call.request.path().startsWith("/static")) {
                "User with session id: $id".log(this::class.java.name).debug()
                call.attributes.put(AttributeKeys.SESSION_ID.value, id)
            }
        }
    }
}

/** Сессионные куки */
data class UserSession(val sessionID: String = UtilitiesLibrary.randomCode(8))