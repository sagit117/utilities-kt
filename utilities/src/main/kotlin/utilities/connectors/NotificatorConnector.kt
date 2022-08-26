/*
 * Copyright (c) 2022.  sagit117@gmail.com <Pavel Aksenov>
 */

package utilities.connectors

import kotlinx.coroutines.*
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
    private var jobSending: Job? = null

    /** Метод добавляет оповещение в очередь и запускает обработчик отправки, если он не был инициирован или не активен */
    suspend fun addQueue(notificationDTO: NotificationDTO) {
        queue.add(notificationDTO)

        if (jobSending == null || !jobSending!!.isActive) {
            "send processing notification start".log(this::class.java.packageName).debug()
            coroutineScope {
                jobSending = launch(Dispatchers.Default) {
                    send()
                }
            }
        }
    }

    /** Обработчик отправок */
    private suspend fun send() {
        delay(delayBeforeSend)

        "try sending".log(this::class.java.packageName).debug()
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
                        "Notification send success".log(this::class.java.packageName).debug()
                    } else {
                        val tmpNotification = queue[0].copy(countSend = queue[0].countSend++)
                        queue.removeAt(0)

                        if (tmpNotification.countSend <= maxCountTrySend) {
                            "Send failed, try count: ${tmpNotification.countSend}".log(this::class.java.packageName).debug()
                            addQueue(tmpNotification)
                        }
                    }
                }

                else -> {
                    "Unknown type notification".log(this::class.java.packageName).error()
                }
            }
        }

        if (queue.isEmpty()) {
            jobSending?.cancel()
            "send processing notification finish".log(this::class.java.packageName).debug()
        } else {
            send()
        }
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

