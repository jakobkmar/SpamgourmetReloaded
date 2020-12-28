package net.axay.spamgourmet.mailserver.mail

import com.mongodb.client.model.ReturnDocument
import kotlinx.coroutines.launch
import net.axay.blueutils.database.mongodb.asKMongoId
import net.axay.spamgourmet.common.data.*
import net.axay.spamgourmet.common.logging.logError
import net.axay.spamgourmet.common.logging.logWarning
import net.axay.spamgourmet.common.main.COROUTINE_SCOPE
import net.axay.spamgourmet.mailserver.main.Constants
import net.axay.spamgourmet.mailserver.main.db
import org.litote.kmongo.*
import org.simplejavamail.api.email.EmailPopulatingBuilder
import org.simplejavamail.converter.EmailConverter
import org.simplejavamail.email.EmailBuilder
import java.time.Instant
import javax.mail.internet.MimeMessage

abstract class SpamgourmetEmail(mimeMessage: MimeMessage) {

    protected val email = EmailConverter.mimeMessageToEmail(mimeMessage)

    abstract suspend fun process(recipient: SpamgourmetAddress)

    companion object {

        fun process(recipients: List<String>, mimeMessage: MimeMessage) {
            recipients.forEach {
                COROUTINE_SCOPE.launch {

                    // get address type
                    val recipient = SpamgourmetAddress(it)
                    val spamgourmetEmail = when (SpamgourmetAddressType.typeOf(null, recipient).recipientType) {
                        SpamgourmetAddressType.SPAMGOURMET_USER_ADDRESS -> SpamgourmetSpamEmail(mimeMessage)
                        SpamgourmetAddressType.SPAMGOURMET_ANSWER_ADDRESS -> SpamgourmetAnswerEmail(mimeMessage)
                        SpamgourmetAddressType.SPAMGOURMET_SPAM_BOUNCE_ADDRESS -> SpamgourmetSpamBounceEmail(mimeMessage)
                        SpamgourmetAddressType.SPAMGOURMET_ANSWER_BOUNCE_ADDRESS -> SpamgourmetAnswerBounceEmail(mimeMessage)
                        else -> null
                    }

                    // process the email
                    try {
                        spamgourmetEmail?.process(recipient)
                    } catch (exc: Exception) {
                        logError("An error occured while processing an email:")
                        exc.printStackTrace()
                    } catch (throwable: Throwable) {
                        logWarning("Something went wrong while processing an email:")
                        throwable.printStackTrace()
                    }

                }
            }
        }

    }

}

class SpamgourmetSpamEmail(mimeMessage: MimeMessage) : SpamgourmetEmail(mimeMessage) {
    override suspend fun process(recipient: SpamgourmetAddress) {

        // FORWARD TO USER

        val username = recipient.firstPartValues[2]

        // only forward if from address is given
        val fromAddress = email.fromRecipient?.address ?: return

        // load user data or return
        val userData = db.userData.findOne(UserData::username eq username) ?: return

        // load user address or create new one
        val userAddressData = db.userAddressData.findOneAndUpdate(
            UserAddressData::address eq recipient.firstPart,
            and(
                setOnInsert(UserAddressData::address, recipient.firstPart),
                setOnInsert(UserAddressData::usesLeft, recipient.firstPartValues[1].toInt())
            ),
            findOneAndUpdateUpsert().returnDocument(ReturnDocument.AFTER)
        ) ?: throw IllegalStateException("Could not find any UserAddressData document in the collection")

        // check uses left
        if (userAddressData.usesLeft <= 0) return

        // check trusted senders
        if (userAddressData.settings?.ifOnlyTrusted == true)
            if (userAddressData.settings?.trustedSenders?.contains(fromAddress) != true) return

        // reduce uses left
        db.userAddressData.updateOne(
                UserAddressData::address eq recipient.firstPart,
                setValue(UserAddressData::usesLeft, userAddressData.usesLeft - 1)
        )

        // load forward recipient
        val forwardToAddress = userAddressData.settings?.alternateForwardTo ?: userData.realAddress

        // load answer address
        val answerAddress = SpamgourmetAddress(
                SpamgourmetAddressGenerator.generateAnswerAddress(
                        username, recipient.firstPart, fromAddress, userAddressData.settings?.alternateForwardTo
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
    override suspend fun process(recipient: SpamgourmetAddress) {

        // ANSWER TO SPAMMER

        // load answer address or return
        val answerAddressData = db.answerAddressData.findOne(AnswerAddressData::address eq recipient.firstPartValues[0])
                ?: return

        // load user data
        val userData = db.userData.findOne(UserData::username eq answerAddressData.forUser) ?: return

        // if locked answer address - check if it comes from the correct address (not safe)
        if (userData.settings.lockedAnswerAddresses) {

            // load from address or return
            val fromAddress = email.fromRecipient?.address ?: return

            if (!(
                fromAddress == userData.realAddress ||
                answerAddressData.settings.additionalUserAddresses.contains(fromAddress)
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
    override suspend fun process(recipient: SpamgourmetAddress) {

        // HANDLE SPAM BOUNCE

        val fromAddress = email.fromRecipient?.address ?: return

        // load bounce address data
        val bounceAddressData = db.spamBounceAddressData.findOne(SpamBounceAddressData::address eq recipient.firstPartValues[0])
                ?: return

        // check if sender address is valid (not safe)
        if (fromAddress != bounceAddressData.userAddress) return

        // save bounce to database
        val id = db.bounceData.insertOne(
            BounceData(
                time = Instant.now(),
                type = BounceType.SPAM_BOUNCE,
                from = fromAddress,
                to = recipient.fullAddress,
                subject = email.subject ?: ""
            )
        ).insertedId?.asKMongoId<BounceData>()
        if (id != null) {
            db.userBounceData.updateOne(
                UserBounceData::username eq bounceAddressData.informUser,
                addToSet(UserBounceData::bounces, id)
            )
        }

    }
}

class SpamgourmetAnswerBounceEmail(mimeMessage: MimeMessage) : SpamgourmetEmail(mimeMessage) {
    override suspend fun process(recipient: SpamgourmetAddress) {

        // HANDLE ANSWER BOUNCE

        // load from address or return
        val fromAddress = email.fromRecipient?.address ?: return

        // load bounce address data
        val bounceAddressData = db.answerBounceAddressData.findOne(
            AnswerBounceAddressData::address eq recipient.firstPartValues[0]
        ) ?: return

        // load user data
        val userData = db.userData.findOne(UserData::username eq bounceAddressData.informUser) ?: return

        // check if sender address is valid (not safe)
        if (fromAddress != bounceAddressData.spammerAddress) return

        // inform user about bounce to answer

        // create mail info about bounce
        val bounceInformationEmail = EmailBuilder.startingBlank()
                .from(SpamgourmetAddress("bounce-informer", true).fullAddress)
                .to(userData.realAddress)
                .withBounceTo("<>")
                .withReplyTo(SpamgourmetAddress(Constants.NO_REPLY_ADDRESS_KEY, true).fullAddress)
                .withSubject("Bounce information")
                .withPlainText("Your answer to $fromAddress got \"answered\" with a bounce.")
                .buildEmail()

        MailSender.sendMail(bounceInformationEmail)

    }
}

private fun EmailPopulatingBuilder.clearSenderData() = clearFromRecipient().clearReplyTo().clearBounceTo()