/*
 * Copyright (c) 2022.  sagit117@gmail.com <Pavel Aksenov>
 */

package utilities.connectors

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import utilities.extensions.log

/** Общий коннектор для рассылки уведомлений.
 * Помещает в очередь уведомление и через время заданное в параметре delayBeforeSend отправляет самое первое вошедшее.
 *
 * @property delayBeforeSend время задержки перед отправкой оповещения
 * @property maxCountTrySend максимальное число попыток отправки уведомления до его удаления
 * @property mailerTransport транспорт для отправки Email
 */
object NotificatorConnector {
    private val queue = mutableListOf<NotificationDTO>()
    var delayBeforeSend = 5000L
    var maxCountTrySend = 5
    var mailerTransport: NotificationTransportConnector = MailerConnector

    suspend fun init(): Unit = coroutineScope { launch(Dispatchers.Default) { send() } }

    fun addQueue(notificationDTO: NotificationDTO): Boolean = queue.add(notificationDTO)

    private suspend fun send() {
        delay(delayBeforeSend)

        queue.firstOrNull()?.let { message ->
            when(message.type) {
                NotificationType.EMAIL -> {
                    if (mailerTransport.send(
                        subject = message.subject,
                        msgHtml = message.message,
                        emails = message.address,
                        altMsgText = null
                    )) {
                        queue.removeAt(0)
                        "Notification send success".log(this::class.java.simpleName).debug()
                    } else {
                        val tmpNotification = queue[0].copy(countSend = queue[0].countSend++)
                        queue.removeAt(0)

                        if (tmpNotification.countSend <= maxCountTrySend) addQueue(tmpNotification)
                    }
                }

                else -> {
                    "Unknown type notification".log(this::class.java.simpleName).error()
                }
            }
        }

        send()
    }
}

@Serializable
data class NotificationDTO(
    val type: NotificationType,
    val message: String,
    val date: String,
    val address: Set<String>,
    val subject: String,
    var countSend: Int = 0,
)

enum class NotificationType {
    EMAIL
}

