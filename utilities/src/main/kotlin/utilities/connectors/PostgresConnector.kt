/*
 * Copyright (c) 2022.  sagit117@gmail.com <Pavel Aksenov>
 */

package utilities.connectors

import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.*
import utilities.extensions.log
import java.sql.Connection

/** Класс хранилище соединений для Postgres */
open class PostgresConnector {
    private val dataSource = HikariDataSource()
    private val connection: () -> Connection = { dataSource.connection }

    fun init(url: String, user: String, password: String) {
        dataSource.jdbcUrl = url
        dataSource.username = user
        dataSource.password = password
    }

    /**
     * Использует выделенное подключение для исполнения кода из параметра block,
     * после выполнения, закрывает соединение.
     *
     * @param block метод будет вызван с соединением к БД.
     * @return результат выполнения кода в block
     */
    suspend fun <T : Any> use(block: suspend (connection: Connection) -> T?): T? = withContext(Dispatchers.IO) {
        val connection = connection()

        connection.autoCommit = false

        val result = try {
            block(connection)
        } catch (e: Throwable) {
            connection.rollback()
            throw e
        }

        connection.commit()
        connection.close()

        return@withContext result
    }
}