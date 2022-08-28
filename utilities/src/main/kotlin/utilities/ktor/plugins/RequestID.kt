/*
 * Copyright (c) 2022.  sagit117@gmail.com <Pavel Aksenov>
 */

package utilities.ktor.plugins

import io.ktor.server.application.*
import utilities.UtilitiesLibrary

/**
 * Плагин формирует ID запроса и сохраняет значение в аттрибуты вызова.
 * Можно запросить call.attributes[AttributeKeys.RESPONSE_ID.value]
 */
val ResponseIdPlugin = createApplicationPlugin(name = "ResponseIdPlugin") {
    onCall { call ->
        call.attributes.put(
            AttributeKeys.RESPONSE_ID.value,
            "${System.currentTimeMillis()}-${UtilitiesLibrary.randomCode(10)}"
        )
    }
}
