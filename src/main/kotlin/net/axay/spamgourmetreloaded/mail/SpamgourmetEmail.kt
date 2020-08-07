package net.axay.spamgourmetreloaded.mail

import net.axay.spamgourmetreloaded.database.data.AnswerAddressData
import net.axay.spamgourmetreloaded.database.data.UserAddressData
import net.axay.spamgourmetreloaded.database.data.UserData
import net.axay.spamgourmetreloaded.main.Manager
import net.axay.spamgourmetreloaded.util.logInfo
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.setValue
import org.simplejavamail.api.email.EmailPopulatingBuilder
import org.simplejavamail.converter.EmailConverter
import org.simplejavamail.email.EmailBuilder
import javax.mail.internet.MimeMessage

abstract class SpamgourmetEmail(mimeMessage: MimeMessage) {

    protected val email = EmailConverter.mimeMessageToEmail(mimeMessage)

    abstract fun process(recipient: SpamgourmetAddress)

    companion object {

        fun process(recipients: List<String>, mimeMessage: MimeMessage) {
            recipients.forEach {
                val recipient = SpamgourmetAddress(it)
                val spamgourmetEmail = when (SpamgourmetAddressType.typeOf(null, recipient).recipientType) {
                    SpamgourmetAddressType.SPAMGOURMET_USER_ADDRESS -> SpamgourmetSpamEmail(mimeMessage)
                    SpamgourmetAddressType.SPAMGOURMET_ANSWER_ADDRESS -> SpamgourmetAnswerEmail(mimeMessage)
                    SpamgourmetAddressType.SPAMGOURMET_BOUNCE_ADDRESS -> SpamgourmetBounceEmail(mimeMessage)
                    else -> null
                }
                try {
                    spamgourmetEmail?.process(recipient)
                } catch (exc: Exception) {
                    logInfo("An error occured while processing an email:", System.err)
                    exc.printStackTrace()
                }
            }
        }

    }

}

class SpamgourmetSpamEmail(mimeMessage: MimeMessage) : SpamgourmetEmail(mimeMessage) {
    override fun process(recipient: SpamgourmetAddress) {

        // FORWARD TO USER

        val username = recipient.firstPartValues[2]

        // load user data or return
        val userData = Manager.dataManager.userCollection.findOne(UserData::username eq username) ?: return

        // load user address or create new one
        val userAddressData = Manager.dataManager.userAddressCollection.findOne(UserAddressData::address eq recipient.firstPart)
                ?: UserAddressData(recipient.firstPart, recipient.firstPartValues[1].toInt()).apply {
                    Manager.dataManager.userAddressCollection.insertOne(this) }

        // check uses left
        if (userAddressData.usesLeft <= 0) return

        // reduce uses left
        Manager.dataManager.userAddressCollection.updateOne(
                UserAddressData::address eq recipient.firstPart,
                setValue(UserAddressData::usesLeft, userAddressData.usesLeft - 1))

        // create forward email
        val emailBuilder = EmailBuilder.copying(email)

        emailBuilder
                .withReplyTo("answeraddress") // TODO set set generated reply address here
                .withBounceTo("spam-bounceaddress") // TODO set generated bounce address here
                .to(userData.realAddress)

        // forward mail
        MailSender.sendMail(emailBuilder.buildEmail())

    }
}

class SpamgourmetAnswerEmail(mimeMessage: MimeMessage) : SpamgourmetEmail(mimeMessage) {
    override fun process(recipient: SpamgourmetAddress) {

        // ANSWER TO SPAMMER

        // load answer address or return
        val answerAddressData = Manager.dataManager.answerAddressCollection.findOne(AnswerAddressData::address eq recipient.firstPart)
                ?: return

        // check if it comes from the correct address (not safe)
        if (email.fromRecipient?.address != answerAddressData.forAddress) return

        // create answer email
        val emailBuilder = EmailBuilder.copying(email)

        emailBuilder
                .clearSenderData()
                .from(answerAddressData.answerAsAddress)
                .withReplyTo(answerAddressData.answerAsAddress)
                .withBounceTo("answer-bounceaddress") // TODO set generated bounce address here
                .to(answerAddressData.answerToAddress)

        // send answer mail
        MailSender.sendMail(emailBuilder.buildEmail())

    }
}

class SpamgourmetBounceEmail(mimeMessage: MimeMessage) : SpamgourmetEmail(mimeMessage) {
    override fun process(recipient: SpamgourmetAddress) {

        // HANDLE BOUNCE

    }
}

private fun EmailPopulatingBuilder.clearSenderData() = clearFromRecipient().clearReplyTo().clearBounceTo()