package net.axay.spamgourmet.mailserver.mail

import com.mongodb.client.MongoCollection
import net.axay.spamgourmet.common.database.contains
import net.axay.spamgourmet.database.data.AnswerAddressData
import net.axay.spamgourmet.database.data.BounceAddressData
import net.axay.spamgourmet.mailserver.main.Manager
import net.axay.spamgourmet.mailserver.main.ValueHolder
import org.bson.conversions.Bson
import org.litote.kmongo.and
import org.litote.kmongo.findOne
import java.security.MessageDigest
import javax.xml.bind.DatatypeConverter

object SpamgourmetAddressGenerator {

    fun generateAnswerAddress(username: String, answerAsAddress: String, answerToAddress: String, alternativeAllowed: String?): String {

        return "${
            findKeyOrGenerateNewOneFromStringWhile(
                    username + answerToAddress,
                    Manager.dataManager.answerAddressCollection,
                    and(
                            AnswerAddressData::forUser eq username,
                            AnswerAddressData::answerAsAddress eq answerAsAddress,
                            AnswerAddressData::answerToAddress eq answerToAddress
                    ),
                    { "${it.address}.${ValueHolder.ANSWER_ADDRESS_KEY}" },
                    { AnswerAddressData::address eq it },
                    { key -> AnswerAddressData(key, answerAsAddress, answerToAddress, username, alternativeAllowed?.let { listOf(it) }) }
            )
        }.${ValueHolder.ANSWER_ADDRESS_KEY}"

    }

    fun generateSpamBounceAddress(username: String, spamgourmetUserAddress: String, userAddress: String): String {

        return "${
            findKeyOrGenerateNewOneFromStringWhile(
                    username + spamgourmetUserAddress,
                    Manager.dataManager.spamBounceAddressCollection,
                    and(
                            BounceAddressData::informUser eq username,
                            BounceAddressData::forAddress eq spamgourmetUserAddress
                    ),
                    { "${it.address}.${ValueHolder.SPAM_BOUNCE_ADDRESS_KEY}" },
                    { BounceAddressData::address eq it },
                    { BounceAddressData(it, username, spamgourmetUserAddress, userAddress) }
            )
        }.${ValueHolder.SPAM_BOUNCE_ADDRESS_KEY}"

    }

    fun generateAnswerBounceAddress(username: String, spammerAddress: String): String {

        return "${
            findKeyOrGenerateNewOneFromStringWhile(
                    username + spammerAddress,
                    Manager.dataManager.answerBounceAddressCollection,
                    and(
                            BounceAddressData::informUser eq username,
                            BounceAddressData::forAddress eq spammerAddress
                    ),
                    { "${it.address}.${ValueHolder.ANSWER_BOUNCE_ADDRESS_KEY}" },
                    { BounceAddressData::address eq it },
                    { BounceAddressData(it, username, spammerAddress, spammerAddress) }
            )
        }.${ValueHolder.ANSWER_BOUNCE_ADDRESS_KEY}"

    }

    private inline fun <E> findKeyOrGenerateNewOneFromStringWhile(
            string: String,
            collection: MongoCollection<E>,
            filter: Bson,
            crossinline ifFound: (foundObject: E) -> String,
            crossinline generateWhile: (currentKey: String) -> Bson,
            crossinline objectToInsert: (key: String) -> E
    ): String {

        // TODO multithreading critical point

        collection.findOne(filter)?.let { return ifFound.invoke(it) }

        val key = generateKeyFromStringWhile(string) {
            collection.contains(generateWhile.invoke(it))
        }
        collection.insertOne(objectToInsert.invoke(key))

        return key

    }

    private fun generateKeyFromStringWhile(string: String, condition: (currentKey: String) -> Boolean): String {

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
