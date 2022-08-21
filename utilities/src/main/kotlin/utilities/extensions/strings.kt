/*
 * Copyright (c) 2022.  sagit117@gmail.com <Pavel Aksenov>
 */

package utilities.extensions

import utilities.Logger
import utilities.UtilitiesLibrary.Companion.hash
import utilities.exceptions.EmailException
import utilities.pattern.regex.emailPattern

/**
 * Проверка на корректность email
 *
 * @return email
 *
 * @throws EmailException если email не проходит проверку
 */
fun String.isEmail(): String {
    return if (emailPattern().matcher(this).matches()) this else throw EmailException()
}

/** Логирование строки */
fun String.log(moduleName: String): Logger = Logger(moduleName, this)

/**
 * Закодировать строку в sha256
 */
fun String.sha256(): String = hash(this)
