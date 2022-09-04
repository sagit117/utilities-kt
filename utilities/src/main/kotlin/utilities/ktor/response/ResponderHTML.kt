/*
 * Copyright (c) 2022.  sagit117@gmail.com <Pavel Aksenov>
 */

package utilities.ktor.response

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import utilities.templating.layouts.BaseLayout
import kotlin.reflect.full.primaryConstructor

/** Декларативный подход к отдаче страниц */
class ResponderHTML(val call: ApplicationCall) {
    private var errorMethod: ((Throwable) -> Unit)? = null
    private var preprocessingMethod: (suspend () -> Unit)? = null
    var layoutComponent: BaseLayout? = null

    /** Предоставления ответа на запрос */
    suspend fun response(layoutInit: BaseLayout.() -> Unit) {
        try { // пытаемся обработать preProcessing
            preprocessingMethod?.invoke()
        } catch (error: Throwable) { // в случае исключения обрабатываем onError
            if (errorMethod == null) throw error
            else errorMethod!!.invoke(error)
        }

        if (layoutComponent != null) {
            call.respondHtmlTemplate(layoutComponent!!, HttpStatusCode.OK, layoutInit)
        }
    }

    /** Действия в случае ошибки, перехват и обработка исключений */
    fun onError(block: (Throwable) -> Unit) {
        errorMethod = block
    }

    /** Пред обработка ответа */
    fun preProcessing(block: suspend () -> Unit) {
        preprocessingMethod = block
    }

    /** Настройка layout */
    inline fun <reified T: BaseLayout> layout(noinline init: T.() -> Unit) {
        val constructor = T::class.primaryConstructor
        val layoutInstance = constructor?.call(call, init)

        layoutComponent = layoutInstance ?: throw Exception("Layout primary constructor is required!")
    }
}