package net.axay.spamgourmet.mail

import net.axay.spamgourmet.main.ValueHolder
import org.subethamail.smtp.MessageContext
import org.subethamail.smtp.MessageHandler
import org.subethamail.smtp.MessageHandlerFactory
import java.io.InputStream
import javax.mail.internet.MimeMessage

class MailHandler : MessageHandlerFactory {

    private val registeredListeners = LinkedHashSet<MailListener>()

    override fun create(context: MessageContext): MessageHandler {

        return object : MessageHandler {

            val mail = Mail()

            override fun from(from: String) {
                mail.sender = from
            }

            override fun recipient(recipient: String) {
                mail.addRecipient(recipient)
            }

            override fun data(data: InputStream) {
                mail.message = MimeMessage(ValueHolder.DEFAULT_MAIL_SESSION, data)
            }

            override fun done() {
                if (mail.validate()) {

                    registeredListeners.forEach {
                        it.onReceive(
                            mail.getValidatedSender(),
                            mail.getValidatedRecipients(),
                            mail.getValidatedMessage()
                        )
                    }

                }
            }

        }

    }

    fun register(mailListener: MailListener) {
        registeredListeners += mailListener
    }

}

interface MailListener {
    fun onReceive(envelopeFrom: String, recipients: List<String>, mimeMessage: MimeMessage)
}