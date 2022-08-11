/*
 * Copyright (c) 2022.  sagit117@gmail.com <Pavel Aksenov>
 */

package utilities

import utilities.exceptions.EmailException
import utilities.extensions.isEmail

/** Класс для размещения вспомогательных методов */
class UtilitiesLibrary {
    /**
     * Генерация случайного хэша заданной длины
     *
     * @param length длина хеша
     * @return строка хэша
     */
    fun randomCode(length: Int): String {
        val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

        return (1..length)
            .map { kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("");
    }

    /** Вспомогательные value классы */
    companion object Values {
        /**
         * Класс для хранения и валидации Email
         *
         * @property email
         * @throws EmailException если email не пройдет валидацию
         */
        @JvmInline
        value class Email(val email: String) {
            init {
                email.isEmail()
            }
        }
    }
}
