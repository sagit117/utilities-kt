/*
 * Copyright (c) 2022.  sagit117@gmail.com <Pavel Aksenov>
 */

package utilities.connectors

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import utilities.extensions.log

/** DSL клиент для http запросов */
open class ClientRequest(val client: HttpClient) {
    /** get request */
    suspend inline fun requestGet(path: String, crossinline init: Resolver.() -> Unit) =
        runBlocking(Dispatchers.IO) {
            val resolver = Resolver {
                client.get(path)
            }
            resolver.init()
            resolver.exec()
        }

    /** post request */
    suspend inline fun <reified T : Any> requestPost(path: String, body: T, crossinline init: Resolver.() -> Unit) =
        runBlocking(Dispatchers.IO) {
            val resolver = Resolver {
                client.post(path) {
                    contentType(ContentType.Application.Json)
                    setBody(body)
                }
            }
            resolver.init()
            resolver.exec()
        }
}

class Resolver(private val query: suspend () -> HttpResponse) {
    lateinit var successCb: suspend (HttpResponse) -> Unit
    lateinit var rejectCb: suspend (HttpResponse) -> Unit

    suspend fun exec() {
        val response = query.invoke()

        if (response.status == HttpStatusCode.OK) {
            "statusOK ${response.status} ${HttpStatusCode.OK} ${response.status == HttpStatusCode.OK}".log(this::class.java.packageName).info()
            successCb(response)
        } else {
            "statusERROR ${response.status} ${HttpStatusCode.OK} ${response.status == HttpStatusCode.OK}".log(this.javaClass.packageName).info()
            rejectCb(response)
        }
    }

    fun success(success: suspend (HttpResponse) -> Unit) {
        successCb = success
    }

    fun reject(reject: suspend (HttpResponse) -> Unit) {
        rejectCb = reject
    }
}
