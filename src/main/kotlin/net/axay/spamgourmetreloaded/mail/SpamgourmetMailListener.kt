package net.axay.spamgourmetreloaded.mail

import javax.mail.internet.MimeMessage

object SpamgourmetMailListener : MailListener {

    override fun onReceive(envelopeFrom: String, recipients: List<String>, mimeMessage: MimeMessage) {
        SpamgourmetEmail.process(recipients, mimeMessage)
    }

}