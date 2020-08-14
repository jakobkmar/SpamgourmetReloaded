package net.axay.spamgourmetreloaded.mail

import net.axay.spamgourmetreloaded.database.contains
import net.axay.spamgourmetreloaded.database.data.AnswerAddressData
import net.axay.spamgourmetreloaded.database.data.BounceAddressData
import net.axay.spamgourmetreloaded.main.Manager
import net.axay.spamgourmetreloaded.main.ValueHolder
import org.litote.kmongo.and
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import java.math.BigInteger
import java.security.MessageDigest
import javax.xml.bind.DatatypeConverter
import kotlin.random.Random
import kotlin.random.nextLong

object SpamgourmetAddressGenerator {

    fun generateAnswerAddress(username: String, answerAsAddress: String, answerToAddress: String, alternativeAllowed: String?): String {

        Manager.dataManager.answerAddressCollection.findOne(
                and(
                        AnswerAddressData::forUser eq username,
                        AnswerAddressData::answerAsAddress eq answerAsAddress,
                        AnswerAddressData::answerToAddress eq answerToAddress
                )
        )?.let { return "${it.address}.${ValueHolder.ANSWER_ADDRESS_KEY}" }

        return "${
            generateKeyFromStringWhile(username + answerToAddress) {
                Manager.dataManager.answerAddressCollection.contains(AnswerAddressData::address eq it)
            }.apply { 
                Manager.dataManager.answerAddressCollection.insertOne(AnswerAddressData(
                        this, answerAsAddress, answerToAddress, username, alternativeAllowed?.let { listOf(it) }
                ))
            }
        }.${ValueHolder.ANSWER_ADDRESS_KEY}"

    }

    fun generateSpamBounceAddress(username: String, spamgourmetUserAddress: String, userAddress: String): String {

        Manager.dataManager.spamBounceAddressCollection.findOne(
                and(
                        BounceAddressData::informUser eq username,
                        BounceAddressData::forAddress eq spamgourmetUserAddress
                )
        )?.let { return "${it.address}.${ValueHolder.SPAM_BOUNCE_ADDRESS_KEY}" }

        return "${
            generateKeyFromStringWhile(username + spamgourmetUserAddress) {
                Manager.dataManager.spamBounceAddressCollection.contains(BounceAddressData::address eq it)
            }.apply { 
                Manager.dataManager.spamBounceAddressCollection.insertOne(BounceAddressData(
                        this, username, spamgourmetUserAddress, userAddress
                ))
            }
        }.${ValueHolder.SPAM_BOUNCE_ADDRESS_KEY}"

    }

    fun generateAnswerBounceAddress(username: String, spammerAddress: String): String {

        Manager.dataManager.answerBounceAddressCollection.findOne(
                and(
                        BounceAddressData::informUser eq username,
                        BounceAddressData::forAddress eq spammerAddress
                )
        )?.let { return "${it.address}.${ValueHolder.ANSWER_BOUNCE_ADDRESS_KEY}" }

        return "${
            generateKeyFromStringWhile(username + spammerAddress) {
                Manager.dataManager.spamBounceAddressCollection.contains(BounceAddressData::address eq it)
            }.apply { 
                Manager.dataManager.answerBounceAddressCollection.insertOne(BounceAddressData(
                        this, username, spammerAddress, spammerAddress
                ))
            }
        }.${ValueHolder.ANSWER_BOUNCE_ADDRESS_KEY}"

    }

    private fun generateKeyFromStringWhile(string: String, condition: (currentKey: String) -> Boolean): String {

        var key = string.md5()

        while (condition.invoke(key))
            key = (key + key.hashCode()).md5()

        return key

    }

    // TODO do not use hashCode() <- implementation not consistent
    // TODO decide for an algorithm to generate answer addresses
    /**
     * it has to be:
     *  - non reversible
     *  - relatively short
     *  TODO find out how the original spamgourmet did it
     */
    private fun generateKeyFromString(string: String): String {

        val weakSeed = string.hashCode()
        val weakRandom = Random(weakSeed)

        val strongSeed = kotlin.run {
            val builder = StringBuilder()
            while (builder.length < 18)
                builder.append(Random(string[weakRandom.nextInt(string.length)].hashCode()).nextInt(10).toString())
            return@run if (builder.length <= 18) builder.toString() else builder.toString().slice(0 until 19)
        }
        val strongRandom = Random(strongSeed.toLong())

        val key = strongRandom.nextLong(1000_0000_0000_0000 until 1_0000_0000_0000_0001)
        return if (key == 1_0000_0000_0000_0000) "0000000000000000" else key.toString()

    }

    private fun String.md5(): String = hashWith("MD5")

    private fun String.hashWith(algorithm: String): String {
        MessageDigest.getInstance(algorithm).let {
            it.update(this.toByteArray())
            return DatatypeConverter.printHexBinary(it.digest())
        }
    }

}
