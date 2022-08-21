/*
 * Copyright (c) 2022.  sagit117@gmail.com <Pavel Aksenov>
 */

package utilities

import utilities.exceptions.EmailException
import utilities.extensions.isEmail
import java.security.MessageDigest

/** Класс для размещения вспомогательных методов */
class UtilitiesLibrary {
    companion object {
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

        /**
         * Функции хеширования SHA-256
         *
         * @param input строка для шифрования
         * @return зашифрованная строка
         */
        fun hash(input: String): String {
            val bytes = input.toByteArray()
            val md = MessageDigest.getInstance("SHA-256")
            val digest = md.digest(bytes)

            return digest.fold("") { str, it -> str + "%02x".format(it) }
        }

        /** Вспомогательные value классы */
        object ValueClasses {
            /**
             * Класс для хранения и валидации Email
             *
             * @property email
             * @throws EmailException если email не пройдет валидацию
             */
            @JvmInline
            value class Email(/** not private */val email: String) {
                init {
                    email.isEmail()
                }
            }
        }
    }
}
