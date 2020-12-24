package net.axay.spamgourmet.mailserver.mail

import javax.mail.internet.MimeMessage

private const val VALIDATE_FIRST_ERROR = "Call validate() on mail object before using this method!"

/**
 * This mail class represents an incoming
 * mail at the [org.subethamail.smtp.MessageHandler].
 */
class Mail {

    var sender: String? = null
    var recipients: MutableList<String>? = null
    var message: MimeMessage? = null

    fun addRecipient(recipient: String) {
        this.recipients?.let {
            it += recipient
            return
        }
        this.recipients = mutableListOf(recipient)
    }

    /**
     * @return wether all mail data is set (true) or not (false)
     */
    fun validate(): Boolean {
        return sender != null && recipients != null && message != null
    }

    fun getValidatedSender() = sender ?: throw IllegalStateException(VALIDATE_FIRST_ERROR)
    fun getValidatedRecipients() = recipients ?: throw IllegalStateException(VALIDATE_FIRST_ERROR)
    fun getValidatedMessage() = message ?: throw IllegalStateException(VALIDATE_FIRST_ERROR)

}