package net.axay.spamgourmet.mailserver.mail

import net.axay.blueutils.database.mongodb.contains
import net.axay.spamgourmet.common.data.AnswerAddressData
import net.axay.spamgourmet.common.data.AnswerAddressSettings
import net.axay.spamgourmet.common.data.AnswerBounceAddressData
import net.axay.spamgourmet.common.data.SpamBounceAddressData
import net.axay.spamgourmet.mailserver.main.Constants
import net.axay.spamgourmet.mailserver.main.db
import org.bson.conversions.Bson
import org.litote.kmongo.and
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.eq
import java.security.MessageDigest
import javax.xml.bind.DatatypeConverter

object SpamgourmetAddressGenerator {

    suspend fun generateAnswerAddress(
        username: String,
        answerAsAddress: String,
        answerToAddress: String,
        alternativeForwardTo: String?
    ): String {

        return "${
            findKeyOrGenerateNewOneFromStringWhile(
                username + answerToAddress,
                db.answerAddressData,
                and(
                    AnswerAddressData::forUser eq username,
                    AnswerAddressData::answerAsAddress eq answerAsAddress,
                    AnswerAddressData::answerToAddress eq answerToAddress
                ),
                { "${it.address}.${Constants.ANSWER_ADDRESS_KEY}" },
                { AnswerAddressData::address eq it },
                {
                    AnswerAddressData(
                        it, answerAsAddress, answerToAddress, username,
                        AnswerAddressSettings(
                            additionalUserAddresses = if (alternativeForwardTo != null)
                                linkedSetOf(alternativeForwardTo)
                            else LinkedHashSet()
                        )
                    )
                }
            )
        }.${Constants.ANSWER_ADDRESS_KEY}"

    }

    suspend fun generateSpamBounceAddress(
        username: String,
        userAddress: String
    ): String {

        return "${
            findKeyOrGenerateNewOneFromStringWhile(
                username + userAddress,
                db.spamBounceAddressData,
                and(
                    SpamBounceAddressData::informUser eq username,
                    SpamBounceAddressData::userAddress eq userAddress
                ),
                { "${it.address}.${Constants.SPAM_BOUNCE_ADDRESS_KEY}" },
                { SpamBounceAddressData::address eq it },
                { SpamBounceAddressData(it, username, userAddress) }
            )
        }.${Constants.SPAM_BOUNCE_ADDRESS_KEY}"

    }

    suspend fun generateAnswerBounceAddress(username: String, spammerAddress: String): String {

        return "${
            findKeyOrGenerateNewOneFromStringWhile(
                username + spammerAddress,
                db.answerBounceAddressData,
                and(
                    AnswerBounceAddressData::informUser eq username,
                    AnswerBounceAddressData::spammerAddress eq spammerAddress
                ),
                { "${it.address}.${Constants.ANSWER_BOUNCE_ADDRESS_KEY}" },
                { AnswerBounceAddressData::address eq it },
                { AnswerBounceAddressData(it, username, spammerAddress, spammerAddress) }
            )
        }.${Constants.ANSWER_BOUNCE_ADDRESS_KEY}"

    }

    private suspend inline fun <E : Any> findKeyOrGenerateNewOneFromStringWhile(
        string: String,
        collection: CoroutineCollection<E>,
        filter: Bson,
        crossinline ifFound: (foundObject: E) -> String,
        crossinline generateWhile: (currentKey: String) -> Bson,
        crossinline objectToInsert: (key: String) -> E
    ): String {

        val kay = collection.findOne(filter)
        if (kay != null)
            return ifFound.invoke(kay)

        val key = generateKeyFromStringWhile(string) {
            collection.contains(generateWhile.invoke(it))
        }
        collection.insertOne(objectToInsert.invoke(key))

        return key

    }

    private suspend fun generateKeyFromStringWhile(
        string: String,
        condition: suspend (currentKey: String) -> Boolean
    ): String {

        var key = string.md5()

        while (condition.invoke(key))
            key = (key + key.hashCode()).md5()

        return key

    }

    private fun String.md5(): String = hashWith("MD5")

    private fun String.hashWith(algorithm: String): String {
        MessageDigest.getInstance(algorithm).let {
            it.update(this.toByteArray())
            return DatatypeConverter.printHexBinary(it.digest())
        }
    }

}