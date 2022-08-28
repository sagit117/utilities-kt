/*
 * Copyright (c) 2022.  sagit117@gmail.com <Pavel Aksenov>
 */

package utilities.connectors

import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.HtmlEmail
import utilities.extensions.log

/** Коннектор для отправки Email */
object MailerConnector: NotificationTransportConnector {
    private lateinit var config: ConfigMailer
    override fun init(configMailer: ConfigMailer): Boolean {
        config = configMailer

        /** Тестовое письмо */
        return send(
            "Init service notification",
            "Service notification init success!",
            setOf(configMailer.from),
            null
        )
    }

    private fun buildEmail(): HtmlEmail {
        val email = HtmlEmail()
        email.hostName = config.hostName
        email.setSmtpPort(config.smtpPort)
        email.setAuthenticator(DefaultAuthenticator(config.user, config.password))
        email.isSSLOnConnect = config.isSSLOnConnect
        email.setFrom(config.from)
        email.setCharset(config.charSet)
        email.setDebug(config.debug)

        return email
    }

    override fun send(
        subject: String,
        msgHtml: String,
        emails: Set<String>,
        altMsgText: String?
    ): Boolean {
        val email = buildEmail()
        email.subject = subject
        email.setMsg(msgHtml)
        email.setTextMsg(altMsgText ?: "Your email client does not support HTML messages")

        for (mail in emails) {
            email.addTo(mail)
        }

        email.buildMimeMessage()

        return try {
            email.sendMimeMessage()
            true
        } catch (error: Throwable) {
            "Sending email is error: $error, cause: ${error.cause}".log(this::class.java.name).error()
            false
        }
    }
}

/**
 * Класс хранения настроек подключения к email-серверу
 *
 * @property hostName адрес сервера.
 * @property smtpPort порт.
 * @property user имя пользователя.
 * @property password пароль пользователя.
 * @property isSSLOnConnect шифрованное соединение.
 * @property from на указанный адрес будет отправлено тестовое сообщение.
 * @property charSet кодировка.
 * @property debug флаг отладки.
 */
data class ConfigMailer(
    val hostName: String,
    val smtpPort: Int,
    val user: String,
    val password: String,
    val isSSLOnConnect: Boolean,
    val from: String,
    val charSet: String,
    val debug: Boolean,
)

interface NotificationTransportConnector {
    fun init(configMailer: ConfigMailer): Boolean
    fun send(subject: String, msgHtml: String, emails: Set<String>, altMsgText: String?): Boolean
}

