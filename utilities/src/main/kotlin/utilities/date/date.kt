/*
 * Copyright (c) 2022.  sagit117@gmail.com <Pavel Aksenov>
 */

package utilities.date

import java.util.*

/**
 * Генерация даты из срока жизни + текущая дата
 *
 * @param expiresAt срок жизни
 *
 * @return дата
 */
fun generateDateFromExpiredAt(expiresAtMs: Long): Date {
    return Date(System.currentTimeMillis() + expiresAtMs)
}
