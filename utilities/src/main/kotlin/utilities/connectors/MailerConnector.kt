/*
 * Copyright (c) 2022.  sagit117@gmail.com <Pavel Aksenov>
 */

package utilities.connectors

import kotlinx.serialization.Serializable
import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.HtmlEmail
import utilities.extensions.log
import javax.mail.Message

/** Коннектор для отправки Email */
object MailerConnector: NotificationTransportConnector {
    private val email = HtmlEmail()

    override fun init(configMailer: ConfigMailer): Boolean {
        email.hostName = configMailer.hostName
        email.setSmtpPort(configMailer.smtpPort)
        email.setAuthenticator(DefaultAuthenticator(configMailer.user, configMailer.password))
        email.isSSLOnConnect = configMailer.isSSLOnConnect
        email.setFrom(configMailer.from)
        email.setCharset(configMailer.charSet)
        email.setDebug(configMailer.debug)

        /** Тестовое письмо */
        return send(
            "Init service notification",
            "Service notification init success!",
            setOf(configMailer.from),
            null
        )
    }

    override fun send(
        subject: String,
        msgHtml: String,
        emails: Set<String>,
        altMsgText: String?
    ): Boolean {
        if (email.mimeMessage == null) {
            email.subject = subject
            email.setMsg(msgHtml)
            email.setTextMsg(altMsgText ?: "Your email client does not support HTML messages")

            for (mail in emails) {
                email.addTo(mail)
            }

            email.buildMimeMessage()
        } else {
            email.mimeMessage.subject = subject
            email.mimeMessage.setContent(msgHtml, "text/html; charset=utf-8")
            email.mimeMessage.setRecipients(Message.RecipientType.TO, emptyArray())

            for (mail in emails) {
                email.mimeMessage.addRecipients(Message.RecipientType.TO, mail)
            }

            email.mimeMessage.saveChanges()
        }

        return try {
            email.sendMimeMessage()

            true
        } catch (error: Throwable) {
            "Sending email is error: $error, cause: ${error.cause}".log(this::class.java.simpleName).error()

            false
        }
    }
}

@Serializable
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

