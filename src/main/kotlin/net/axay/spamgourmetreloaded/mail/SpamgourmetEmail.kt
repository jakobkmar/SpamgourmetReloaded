package net.axay.spamgourmetreloaded.mail

import net.axay.spamgourmetreloaded.database.DatabaseQueries
import net.axay.spamgourmetreloaded.database.data.*
import net.axay.spamgourmetreloaded.main.Manager
import net.axay.spamgourmetreloaded.util.logInfo
import org.litote.kmongo.*
import org.simplejavamail.api.email.EmailPopulatingBuilder
import org.simplejavamail.converter.EmailConverter
import org.simplejavamail.email.EmailBuilder
import java.time.Instant
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
                    SpamgourmetAddressType.SPAMGOURMET_SPAM_BOUNCE_ADDRESS -> SpamgourmetSpamBounceEmail(mimeMessage)
                    SpamgourmetAddressType.SPAMGOURMET_ANSWER_BOUNCE_ADDRESS -> SpamgourmetAnswerBounceEmail(mimeMessage)
                    else -> null
                }
                try {
                    spamgourmetEmail?.process(recipient)
                } catch (exc: Throwable) {
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
        val userData = DatabaseQueries.getUserDataFromUsername(username) ?: return

        // load user address or create new one
        val userAddressData = Manager.dataManager.userAddressCollection.findOne(UserAddressData::address eq recipient.firstPart)
                ?: UserAddressData(recipient.firstPart, recipient.firstPartValues[1].toInt()).apply {
                    Manager.dataManager.userAddressCollection.insertOne(this) }

        // check uses left
        if (userAddressData.usesLeft <= 0) return

        // check trusted senders
        if (userAddressData.ifOnlyTrusted == true) {
            val fromAddress = email.fromRecipient?.address ?: return
            if (userAddressData.trustedSenders?.contains(fromAddress) != true) return
        }

        // reduce uses left
        Manager.dataManager.userAddressCollection.updateOne(
                UserAddressData::address eq recipient.firstPart,
                setValue(UserAddressData::usesLeft, userAddressData.usesLeft - 1))

        // load forward recipient
        val forwardToAddress = userAddressData.alternativeForwardTo ?: userData.realAddress

        // create forward email
        val emailBuilder = EmailBuilder.copying(email)

        emailBuilder
                .clearRecipients()
                .withReplyTo("answeraddress") // TODO set set generated reply address here
                .withBounceTo("spam-bounceaddress") // TODO set generated bounce address here
                .to(forwardToAddress)

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

        // load user data
        val userData = DatabaseQueries.getUserDataFromUsername(answerAddressData.forUser) ?: return
        // TODO delete answer address if user does not exists anymore

        // if locked answer address - check if it comes from the correct address (not safe)
        if (userData.settings.lockedAnswerAddress == true) {
            if (email.fromRecipient?.address != userData.realAddress) return
        }

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

class SpamgourmetSpamBounceEmail(mimeMessage: MimeMessage) : SpamgourmetEmail(mimeMessage) {
    override fun process(recipient: SpamgourmetAddress) {

        // HANDLE SPAM BOUNCE

        // load bounce address data
        val bounceAddressData = Manager.dataManager.spamBounceAddressCollection.findOne(BounceAddressData::address eq recipient.firstPart)
                ?: return

        // load user data
        val userData = DatabaseQueries.getUserDataFromUsername(bounceAddressData.informUser) ?: return // TODO delete this address

        // check if sender address is valid (not safe)
        if (email.fromRecipient?.address != userData.realAddress) return

        // save bounce to database
        Manager.dataManager.userCollection.updateOne(
                UserData::username eq bounceAddressData.informUser,                       // TODO
                push(UserData::bounceData, BounceData(Instant.now(), email.fromRecipient?.address.toString()))
        )

    }
}

class SpamgourmetAnswerBounceEmail(mimeMessage: MimeMessage) : SpamgourmetEmail(mimeMessage) {
    override fun process(recipient: SpamgourmetAddress) {

        // HANDLE ANSWER BOUNCE

        // load bounce address data
        val bounceAddressData = Manager.dataManager.answerBounceAddressCollection.findOne(BounceAddressData::address eq recipient.firstPart)
                ?: return

        // load user data
        val userData = DatabaseQueries.getUserDataFromUsername(bounceAddressData.informUser) ?: return // TODO delete this address

        // check if sender address is valid (not safe)
        if (email.fromRecipient?.address != userData.realAddress) return

        // inform user about bounce to answer

        // create info about bounce
        val bounceInformationEmail = EmailBuilder.startingBlank()
                .from("bounce-informer@" + Manager.configManager.mainConfig.addressDomain)
                .to(userData.realAddress)
                .withSubject("Bounce information")
                .withPlainText("Your answer to ${recipient.fullAddress} got \"answered\" with a bounce.")
                .buildEmail()

        MailSender.sendMail(bounceInformationEmail)

    }
}

private fun EmailPopulatingBuilder.clearSenderData() = clearFromRecipient().clearReplyTo().clearBounceTo()