/*
 * Copyright (c) 2022.  sagit117@gmail.com <Pavel Aksenov>
 */

package utilities.connectors

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import utilities.extensions.log

/** Postgres exposed connector */
object PostgresConnector {
    fun init(
        driverClassName: String,
        jdbcURL: String,
        user: String,
        password: String,
        initTables: Transaction.() -> Unit, // метод инициации таблиц
    ) {
        val database = Database.connect(jdbcURL, driverClassName, user, password)

        "Version PostgresSQL: ${database.version}".log(this.javaClass.simpleName).info()

        transaction(database, initTables)
    }

    /**
     * Запрос к БД
     *
     * @param block функция запроса
     */
    fun <T> dbQuery(block: suspend () -> T): T = runBlocking {
        newSuspendedTransaction(Dispatchers.IO) {
            block()
        }
    }
}