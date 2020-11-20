package net.axay.spamgourmet.mail

import net.axay.spamgourmet.database.DatabaseQueries
import net.axay.spamgourmet.database.data.*
import net.axay.spamgourmet.main.Manager
import net.axay.spamgourmet.main.ValueHolder
import net.axay.spamgourmet.util.logInfo
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.push
import org.litote.kmongo.setValue
import org.simplejavamail.api.email.EmailPopulatingBuilder
import org.simplejavamail.converter.EmailConverter
import org.simplejavamail.email.EmailBuilder
import java.time.Instant
import java.util.concurrent.Executors
import javax.mail.internet.MimeMessage

abstract class SpamgourmetEmail(mimeMessage: MimeMessage) {

    protected val email = EmailConverter.mimeMessageToEmail(mimeMessage)

    abstract fun process(recipient: SpamgourmetAddress)

    companion object {

        // TODO maybe use transactions for database operations later
        // this would allow multithreading
        // BUT: -> requires Replica Set
        private val executorService = Executors.newSingleThreadExecutor()

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
                executorService.execute {
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

}

// TODO when user deletes its account -> delete all addresses of him

class SpamgourmetSpamEmail(mimeMessage: MimeMessage) : SpamgourmetEmail(mimeMessage) {
    override fun process(recipient: SpamgourmetAddress) {

        // FORWARD TO USER

        val username = recipient.firstPartValues[2]

        // only forward if from address is given
        val fromAddress = email.fromRecipient?.address ?: return

        // load user data or return
        val userData = DatabaseQueries.getUserDataFromUsername(username) ?: return

        // TODO multithreading critical point
        // load user address or create new one
        val userAddressData =
                Manager.dataManager.userAddressCollection.findOne(UserAddressData::address eq recipient.firstPart)
                    ?: UserAddressData(recipient.firstPart, recipient.firstPartValues[1].toInt()).apply {
                        Manager.dataManager.userAddressCollection.insertOne(this) }

        // check uses left
        if (userAddressData.usesLeft <= 0) return

        // check trusted senders
        if (userAddressData.ifOnlyTrusted == true)
            if (userAddressData.trustedSenders?.contains(fromAddress) != true) return

        // reduce uses left
        Manager.dataManager.userAddressCollection.updateOne(
                UserAddressData::address eq recipient.firstPart,
                setValue(UserAddressData::usesLeft, userAddressData.usesLeft - 1)
        )

        // load forward recipient
        val forwardToAddress = userAddressData.alternativeForwardTo ?: userData.realAddress

        // load answer address
        val answerAddress = SpamgourmetAddress(
                SpamgourmetAddressGenerator.generateAnswerAddress(
                        username, recipient.firstPart, fromAddress, userAddressData.alternativeForwardTo
                ),
                true
        ).fullAddress

        // load bounce address
        val bounceAddress = SpamgourmetAddress(
                SpamgourmetAddressGenerator.generateSpamBounceAddress(username, userAddressData.address, forwardToAddress),
                true
        ).fullAddress

        // create forward email
        val emailBuilder = EmailBuilder.copying(email)

        emailBuilder
                .clearRecipients()
                .withReplyTo(answerAddress)
                .withBounceTo(bounceAddress)
                .to(forwardToAddress)

        // forward mail
        MailSender.sendMail(emailBuilder.buildEmail())

    }
}

class SpamgourmetAnswerEmail(mimeMessage: MimeMessage) : SpamgourmetEmail(mimeMessage) {
    override fun process(recipient: SpamgourmetAddress) {

        // ANSWER TO SPAMMER

        // load answer address or return
        val answerAddressData = Manager.dataManager.answerAddressCollection.findOne(AnswerAddressData::address eq recipient.firstPartValues[0])
                ?: return

        // load user data
        val userData = DatabaseQueries.getUserDataFromUsername(answerAddressData.forUser) ?: return

        // if locked answer address - check if it comes from the correct address (not safe)
        if (userData.settings.lockedAnswerAddresses) {

            // load from address or return
            val fromAddress = email.fromRecipient?.address ?: return

            if (!(
                fromAddress == userData.realAddress ||
                answerAddressData.extraAllowedUserAddresses?.contains(fromAddress) == true
            )) return

        }

        val bounceAddress = SpamgourmetAddress(
                SpamgourmetAddressGenerator.generateAnswerBounceAddress(answerAddressData.forUser, answerAddressData.answerToAddress),
                true
        ).fullAddress

        val answerAsAddress = SpamgourmetAddress(answerAddressData.answerAsAddress, true).fullAddress

        // create answer email
        val emailBuilder = EmailBuilder.copying(email)

        emailBuilder
                .clearSenderData()
                .clearRecipients()
                .from(answerAsAddress)
                .withReplyTo(answerAsAddress)
                .withBounceTo(bounceAddress)
                .to(answerAddressData.answerToAddress)

        // send answer mail
        MailSender.sendMail(emailBuilder.buildEmail())

    }
}

class SpamgourmetSpamBounceEmail(mimeMessage: MimeMessage) : SpamgourmetEmail(mimeMessage) {
    override fun process(recipient: SpamgourmetAddress) {

        // HANDLE SPAM BOUNCE

        val fromAddress = email.fromRecipient?.address ?: return

        // load bounce address data
        val bounceAddressData = Manager.dataManager.spamBounceAddressCollection.findOne(BounceAddressData::address eq recipient.firstPartValues[0])
                ?: return

        // check if sender address is valid (not safe)
        bounceAddressData.restrictedFrom?.let {
            if (fromAddress != it) return
        }

        // save bounce to database
        Manager.dataManager.userCollection.updateOne(
                UserData::username eq bounceAddressData.informUser,
                push(UserData::bounceData, BounceData(
                        Instant.now(),
                        fromAddress,
                        bounceAddressData.forAddress
                ))
        )

    }
}

class SpamgourmetAnswerBounceEmail(mimeMessage: MimeMessage) : SpamgourmetEmail(mimeMessage) {
    override fun process(recipient: SpamgourmetAddress) {

        // HANDLE ANSWER BOUNCE

        // load from address or return
        val fromAddress = email.fromRecipient?.address ?: return

        // load bounce address data
        val bounceAddressData = Manager.dataManager.answerBounceAddressCollection.findOne(BounceAddressData::address eq recipient.firstPartValues[0])
                ?: return

        // load user data
        val userData = DatabaseQueries.getUserDataFromUsername(bounceAddressData.informUser) ?: return

        // check if sender address is valid (not safe)
        if (fromAddress != bounceAddressData.forAddress) return

        // inform user about bounce to answer

        // create mail info about bounce
        val bounceInformationEmail = EmailBuilder.startingBlank()
                .from(SpamgourmetAddress("bounce-informer", true).fullAddress)
                .to(userData.realAddress)
                .withBounceTo("<>")
                .withReplyTo(SpamgourmetAddress(ValueHolder.NO_REPLY_ADDRESS_KEY, true).fullAddress)
                .withSubject("Bounce information")
                .withPlainText("Your answer to $fromAddress got \"answered\" with a bounce.")
                .buildEmail()

        MailSender.sendMail(bounceInformationEmail)

    }
}

private fun EmailPopulatingBuilder.clearSenderData() = clearFromRecipient().clearReplyTo().clearBounceTo()