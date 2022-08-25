/*
 * Copyright (c) 2022.  sagit117@gmail.com <Pavel Aksenov>
 */

package utilities.connectors

import com.zaxxer.hikari.HikariDataSource
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
    fun <T : Any> use(block: (connection: Connection) -> T): T {
        val connection = connection()
        val result = block(connection)

        connection.close()

        return result
    }
}